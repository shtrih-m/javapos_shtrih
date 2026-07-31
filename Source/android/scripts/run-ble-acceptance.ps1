# BLE headless acceptance connect test across Android emulator API levels via Bumble.
# Emulators run without UI: -no-window -no-audio -no-boot-anim
# (this emulator build rejects -screen off; valid: touch|multi-touch|no-touch)
# Usage:
#   powershell -File Source/android/scripts/run-ble-acceptance.ps1 -SkipImageInstall
#
# Requires: Android SDK, AVD system images, Bumble (rtk_netsim_bridge), USB dongle WinUSB.
# Note: Emulator Bluetooth/netsim HCI is supported from API 31+ (Android docs).
#       API 30 and below are skipped — BT enable/scan may look alive but do not
#       receive real HCI ads from the Bumble USB bridge.

param(
    [string[]]$ApiLevels = @("31", "33", "36"),
    [string]$PrinterMac = "30:AE:A4:95:F3:7E",
    [string]$AvdPrefix = "ShtrihBleAccept",
    [int]$BumblePort = 8555,
    [int]$ConnectTimeoutSec = 120,
    [switch]$SkipImageInstall,
    [switch]$KeepEmulators,
    [switch]$StopOnError,
    # Force-run API levels below 31 (emulator netsim HCI is unsupported; expect FAIL).
    [switch]$AllowUnsupportedApi
)

$ErrorActionPreference = "Continue"
# Native tools (adb/emulator) often write to stderr; do not treat as terminating.
# Prefer an SDK that has BOTH adb and emulator (ANDROID_SDK_ROOT may point at a partial install).
$SdkCandidates = @(
    "$env:LOCALAPPDATA\Android\Sdk",
    "C:\Users\User\AppData\Local\Android\Sdk",
    $env:ANDROID_SDK_ROOT,
    $env:ANDROID_HOME,
    "C:\Android\Sdk"
) | Where-Object {
    $_ -and
    (Test-Path (Join-Path $_ "platform-tools\adb.exe")) -and
    (Test-Path (Join-Path $_ "emulator\emulator.exe"))
}
if (-not $SdkCandidates -or $SdkCandidates.Count -eq 0) {
    throw "Android SDK not found (adb.exe + emulator.exe required). Set ANDROID_SDK_ROOT."
}
$Sdk = $SdkCandidates[0]
$env:ANDROID_SDK_ROOT = $Sdk
$env:ANDROID_HOME = $Sdk
$env:SKIP_JDK_VERSION_CHECK = "1"

$Adb = Join-Path $Sdk "platform-tools\adb.exe"
$Emulator = Join-Path $Sdk "emulator\emulator.exe"
$AvdManager = Join-Path $Sdk "cmdline-tools\latest\bin\avdmanager.bat"
$SdkManager = Join-Path $Sdk "cmdline-tools\latest\bin\sdkmanager.bat"
$RepoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..\..")).Path
$AndroidDir = Join-Path $RepoRoot "Source\android"
$OutDir = Join-Path $RepoRoot "Source\android\acceptance-results"
$ApkGlob = Join-Path $AndroidDir "tinyJavaPosTester\build\outputs\apk\debug\*.apk"
$Package = "com.shtrih.tinyjavapostester"
$Activity = "com.shtrih.tinyjavapostester.AcceptanceConnectActivity"
$JavaHome8 = "C:\Program Files\Java\jdk1.8.0_202"
$FirmwareDir = "C:\Users\User\AppData\Local\google\bumble\firmware\realtek"

New-Item -ItemType Directory -Force -Path $OutDir | Out-Null

function Write-Step($msg) { Write-Host ("`n=== {0} ===" -f $msg) -ForegroundColor Cyan }

function Ensure-Images {
    if ($SkipImageInstall) { return }
    Write-Step "Installing system images (if missing)"
    $packages = @()
    foreach ($api in $ApiLevels) {
        $packages += "system-images;android-$api;google_apis;x86_64"
        $packages += "system-images;android-$api;google_apis_playstore;x86_64"
        $packages += "platforms;android-$api"
    }
    $yes = "y`n" * 40
    $packages | ForEach-Object { Write-Host "sdkmanager $_" }
    cmd /c "echo $yes | `"$SdkManager`" --install $($packages -join ' ')"
}

function Ensure-Avd([string]$api) {
    $name = "${AvdPrefix}_API$api"
    $ini = Join-Path $env:USERPROFILE ".android\avd\$name.ini"
    if (Test-Path $ini) {
        return $name
    }
    Write-Step "Creating AVD $name"
    $device = "pixel_4"
    $candidates = @(
        "system-images;android-$api;google_apis;x86_64",
        "system-images;android-$api;google_apis_playstore;x86_64",
        "system-images;android-$api;aosp_atd;x86_64"
    )
    $pkg = $null
    foreach ($c in $candidates) {
        $imgPath = Join-Path $Sdk ("system-images\" + ($c -replace ";","\"))
        if (Test-Path $imgPath) { $pkg = $c; break }
    }
    if (-not $pkg) {
        $pkg = $candidates[0]
        $prevJavaInstall = $env:JAVA_HOME
        if (Test-Path "C:\Program Files\Java\jdk-17") {
            $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
            $env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
        }
        try {
            cmd /c "echo y| `"$SdkManager`" --install `"$pkg`"" | Out-Null
        } finally {
            if ($prevJavaInstall) { $env:JAVA_HOME = $prevJavaInstall }
        }
    }
    $prevJava = $env:JAVA_HOME
    if (Test-Path "C:\Program Files\Java\jdk-17") {
        $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
        $env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
    }
    $env:SKIP_JDK_VERSION_CHECK = "1"
    try {
        # Discard avdmanager stdout so it does not become part of the function return value.
        $null = cmd /c "echo no | `"$AvdManager`" create avd -n $name -k `"$pkg`" -d $device --force 2>&1"
        if (-not (Test-Path $ini)) {
            throw "Failed to create AVD $name with $pkg"
        }
    } finally {
        if ($prevJava) { $env:JAVA_HOME = $prevJava }
    }
    return ,$name
}

function Invoke-Adb {
    param([Parameter(ValueFromRemainingArguments = $true)][string[]]$Args)
    & $Adb @Args 2>&1 | ForEach-Object { "$_" }
}

# Capture adb stdout as UTF-8 without reinterpreting bytes as the OEM code page
# (CP866 on Russian Windows turns "ШТРИХ" into "╨и╨в╨а╨Ш╨е").
function Get-AdbUtf8Text {
    param(
        [Parameter(Mandatory = $true)][string[]]$AdbArgs,
        [switch]$Trim
    )
    $outFile = [System.IO.Path]::GetTempFileName()
    $errFile = [System.IO.Path]::GetTempFileName()
    try {
        $null = Start-Process -FilePath $Adb -ArgumentList $AdbArgs `
            -RedirectStandardOutput $outFile -RedirectStandardError $errFile `
            -NoNewWindow -Wait -PassThru
        $utf8 = New-Object System.Text.UTF8Encoding $false
        $text = [System.IO.File]::ReadAllText($outFile, $utf8)
        if ($Trim) { $text = $text.Trim() }
        return $text
    } finally {
        Remove-Item -Force $outFile, $errFile -ErrorAction SilentlyContinue
    }
}

function Save-AdbLogcat {
    param(
        [Parameter(Mandatory = $true)][string]$Serial,
        [Parameter(Mandatory = $true)][string]$OutPath
    )
    $raw = Get-AdbUtf8Text -AdbArgs @("-s", $Serial, "exec-out", "logcat", "-d", "-v", "threadtime")
    $utf8 = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($OutPath, $raw, $utf8)
    return @($raw -split "`r?`n")
}

function Stop-Emulators {
    $devs = & $Adb devices 2>&1
    foreach ($line in $devs) {
        if ("$line" -match "^(emulator-\d+)\s+device") {
            & $Adb -s $Matches[1] emu kill 2>&1 | Out-Null
        }
    }
    Start-Sleep -Seconds 2
    Get-Process emulator, qemu-system-x86_64 -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
}

function Reset-UsbDongle {
    Write-Step "Resetting Mercusys/Realtek USB Bluetooth dongle"
    $devs = @(Get-PnpDevice -PresentOnly -ErrorAction SilentlyContinue |
        Where-Object { $_.InstanceId -match "VID_2C4E&PID_0115|VID_0BDA&PID_0115" })
    if ($devs.Count -eq 0) {
        Write-Host "WARNING: USB dongle PnP device not found"
        return
    }
    foreach ($d in $devs) {
        Write-Host ("Reset {0}" -f $d.InstanceId)
        Disable-PnpDevice -InstanceId $d.InstanceId -Confirm:$false -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 2
        Enable-PnpDevice -InstanceId $d.InstanceId -Confirm:$false -ErrorAction SilentlyContinue
    }
    Start-Sleep -Seconds 3
}

function Start-Bumble {
    param([switch]$ForceReset)
    Write-Step "Starting RTK-aware Bumble HCI bridge on port $BumblePort"
    $listening = netstat -ano | Select-String (":" + $BumblePort + "\s+.*LISTENING")
    if ($listening -and -not $ForceReset) {
        Write-Host "Reusing existing Bumble on port $BumblePort"
        return
    }
    Get-CimInstance Win32_Process -ErrorAction SilentlyContinue |
        Where-Object { $_.CommandLine -match "bumble-hci-bridge|rtk_netsim_bridge" } |
        ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }
    Start-Sleep -Seconds 3
    # Soft USB reset only when starting a new bridge (avoid wedging Realtek).
    Reset-UsbDongle
    Start-Sleep -Seconds 5

    $bumbleLog = Join-Path $OutDir "bumble.log"
    $bumbleErr = Join-Path $OutDir "bumble.err.log"
    Remove-Item $bumbleLog, $bumbleErr -Force -ErrorAction SilentlyContinue

    $bridgePy = Join-Path $PSScriptRoot "rtk_netsim_bridge.py"
    if (-not (Test-Path $bridgePy)) { throw "Missing $bridgePy" }

    $env:BUMBLE_RTK_FIRMWARE_DIR = $FirmwareDir
    $env:BUMBLE_LOGLEVEL = "INFO"
    $arg1 = "android-netsim:_:" + $BumblePort + ",mode=controller"
    $arg2 = "usb:[driver=rtk]0"
    # Prefer real CPython over the WindowsApps stub (avoids duplicate bridge PIDs).
    $python = $null
    foreach ($c in @(
            "$env:LOCALAPPDATA\Python\pythoncore-3.14-64\python.exe",
            "$env:LOCALAPPDATA\Programs\Python\Python312\python.exe",
            "$env:LOCALAPPDATA\Programs\Python\Python311\python.exe"
        )) {
        if (Test-Path $c) { $python = $c; break }
    }
    if (-not $python) {
        $python = (Get-Command python -ErrorAction SilentlyContinue).Source
    }
    if (-not $python) { throw "python not found in PATH" }

    # -u: unbuffered logs (RedirectStandardError otherwise stays empty until exit).
    Start-Process -FilePath $python `
        -ArgumentList @("-u", $bridgePy, $arg1, $arg2) `
        -RedirectStandardOutput $bumbleLog `
        -RedirectStandardError $bumbleErr `
        -WindowStyle Hidden | Out-Null

    $ok = $false
    for ($i = 0; $i -lt 30; $i++) {
        Start-Sleep -Seconds 2
        $listening = netstat -ano | Select-String (":" + $BumblePort + "\s+.*LISTENING")
        if ($listening) { $ok = $true; break }
    }
    if (-not $ok) {
        $errText = ""
        if (Test-Path $bumbleErr) { $errText = (Get-Content $bumbleErr -Raw) }
        throw "Bumble/RTK bridge is not listening on $BumblePort. $errText"
    }
    Write-Host "RTK Bumble bridge listening on $BumblePort"
}

function Wait-Boot([string]$serial, [int]$timeoutSec = 300) {
    $deadline = (Get-Date).AddSeconds($timeoutSec)
    do {
        $boot = (& $Adb -s $serial shell getprop sys.boot_completed 2>$null | Out-String).Trim()
        if ($boot -eq "1") {
            $svc = (& $Adb -s $serial shell service check activity 2>$null | Out-String)
            $pm = (& $Adb -s $serial shell pm path android 2>$null | Out-String)
            if (($svc -match "found") -and ($pm -match "package:")) {
                # Extra settle: ActivityManager can report "found" briefly before fully ready.
                Start-Sleep -Seconds 8
                $svc2 = (& $Adb -s $serial shell service check activity 2>$null | Out-String)
                if ($svc2 -match "found") { return }
            }
        }
        Start-Sleep -Seconds 3
    } while ((Get-Date) -lt $deadline)
    throw "Boot timeout for $serial"
}

# Map common Windows time zone IDs to Android/zoneinfo names for -timezone / setprop.
function Get-HostZoneInfoId {
    $winId = [TimeZoneInfo]::Local.Id
    $map = @{
        "Russian Standard Time"           = "Europe/Moscow"
        "Belarus Standard Time"           = "Europe/Minsk"
        "FLE Standard Time"               = "Europe/Kiev"
        "GMT Standard Time"               = "Europe/London"
        "W. Europe Standard Time"         = "Europe/Berlin"
        "Central Europe Standard Time"    = "Europe/Prague"
        "Central European Standard Time"  = "Europe/Warsaw"
        "E. Europe Standard Time"         = "Europe/Bucharest"
        "GTB Standard Time"               = "Europe/Athens"
        "UTC"                             = "UTC"
        "Pacific Standard Time"           = "America/Los_Angeles"
        "Mountain Standard Time"          = "America/Denver"
        "Central Standard Time"           = "America/Chicago"
        "Eastern Standard Time"           = "America/New_York"
        "Tokyo Standard Time"             = "Asia/Tokyo"
        "China Standard Time"             = "Asia/Shanghai"
    }
    if ($map.ContainsKey($winId)) { return $map[$winId] }
    return $null
}

function Sync-EmulatorClock([string]$serial) {
    Write-Host "Syncing emulator clock to host"
    & $Adb -s $serial shell settings put global auto_time 0 2>&1 | Out-Null
    & $Adb -s $serial shell settings put global auto_time_zone 0 2>&1 | Out-Null

    $zone = Get-HostZoneInfoId
    if ($zone) {
        & $Adb -s $serial shell setprop persist.sys.timezone $zone 2>&1 | Out-Null
        Write-Host "timezone=$zone"
    }

    $epoch = [DateTimeOffset]::Now.ToUnixTimeSeconds()
    $setOk = $false
    foreach ($cmd in @(
            "su 0 date @$epoch",
            "su root date @$epoch",
            "date @$epoch"
        )) {
        $out = (& $Adb -s $serial shell $cmd 2>&1 | Out-String).Trim()
        if ($out -match "bad date|invalid|Unknown option|Permission denied|not found") {
            continue
        }
        $setOk = $true
        break
    }
    $emuDate = (& $Adb -s $serial shell date 2>&1 | Out-String).Trim()
    $hostDate = (Get-Date).ToString("yyyy-MM-dd HH:mm:ss zzz")
    Write-Host "host=$hostDate"
    Write-Host "emu =$emuDate (setOk=$setOk)"
}

function Grant-Permissions([string]$serial, [int]$apiLevel) {
    $perms = @(
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION"
    )
    if ($apiLevel -ge 29) {
        $perms += "android.permission.ACCESS_BACKGROUND_LOCATION"
    }
    # BLUETOOTH_SCAN/CONNECT exist only on API 31+.
    if ($apiLevel -ge 31) {
        $perms = @(
            "android.permission.BLUETOOTH_CONNECT",
            "android.permission.BLUETOOTH_SCAN"
        ) + $perms
    }
    foreach ($p in $perms) {
        $grantOut = & $Adb -s $serial shell pm grant $Package $p 2>&1 | Out-String
        if ($grantOut -match "Exception|Error|Unknown permission") {
            Write-Host "pm grant $p : $($grantOut.Trim())"
        }
    }
    # BLE scan on Android 6..11 requires location services enabled.
    & $Adb -s $serial shell settings put secure location_mode 3 2>&1 | Out-Null
    & $Adb -s $serial shell settings put secure location_providers_allowed +gps 2>&1 | Out-Null
    & $Adb -s $serial shell settings put secure location_providers_allowed +network 2>&1 | Out-Null
    $locMode = (& $Adb -s $serial shell settings get secure location_mode 2>$null | Out-String).Trim()
    Write-Host "location_mode=$locMode"
    & $Adb -s $serial shell svc bluetooth enable 2>&1 | Out-Null
    & $Adb -s $serial shell cmd bluetooth_manager enable 2>&1 | Out-Null

    $deadline = (Get-Date).AddSeconds(90)
    do {
        Start-Sleep -Seconds 3
        $enabled = (& $Adb -s $serial shell settings get global bluetooth_on 2>$null | Out-String).Trim()
        $bt = & $Adb -s $serial shell dumpsys bluetooth_manager 2>&1 | Out-String
        # Require a real adapter ON state — bluetooth_on=1 alone is not enough when HCI is wedged.
        # Prefer explicit adapter lines; bare "STATE_ON" false-positives are common in dumpsys text.
        $stateOn = ($bt -match "(?i)(?:^|\n)\s*state:\s*ON\s*(?:\r?\n|$)" `
            -or $bt -match "(?i)AdapterState:\s*ON\b" `
            -or $bt -match "(?i)mState\s*=\s*ON\b")
        if ($stateOn) {
            Write-Host "Bluetooth ON (bluetooth_on=$enabled, dumpsys adapter ON)"
            Start-Sleep -Seconds 10
            return
        }
        Write-Host "Waiting for Bluetooth... bluetooth_on=$enabled stateOn=$stateOn"
        # Only enable — cycling disable/enable wedges netsim HCI on API 33/36.
        & $Adb -s $serial shell svc bluetooth enable 2>&1 | Out-Null
        & $Adb -s $serial shell cmd bluetooth_manager enable 2>&1 | Out-Null
    } while ((Get-Date) -lt $deadline)
    Write-Host "WARNING: Bluetooth may still be off on $serial"
    throw "Bluetooth adapter did not reach ON on $serial (HCI/Bumble likely not attached)"
}

function Build-Apk {
    Write-Step "Building tinyJavaPosTester debug APK"
    if (-not (Test-Path $JavaHome8)) { throw "JDK 8 not found: $JavaHome8" }
    $env:JAVA_HOME = $JavaHome8
    $env:PATH = "$JavaHome8\bin;" + $env:PATH
    Push-Location $AndroidDir
    try {
        $null = cmd /c "gradlew.bat :tinyJavaPosTester:assembleDebug --no-daemon > `"$OutDir\gradle-build.log`" 2>&1"
        if ($LASTEXITCODE -ne 0) { throw "Gradle build failed: $LASTEXITCODE (see $OutDir\gradle-build.log)" }
    } finally {
        Pop-Location
    }
    $apk = Get-ChildItem $ApkGlob | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if (-not $apk) { throw "APK not found: $ApkGlob" }
    Write-Host "APK=$($apk.FullName)"
    return $apk.FullName
}

function Run-One([string]$api, [string]$apk) {
    $apiLevel = 0
    [void][int]::TryParse($api, [ref]$apiLevel)
    $avd = Ensure-Avd $api
    $resultFile = Join-Path $OutDir ("API{0}.txt" -f $api)
    Write-Step "API $api / AVD $avd"

    # Emulator Bluetooth (netsim HCI / packet-streamer) is documented for API 31+.
    # On API 30 the adapter can enable and LE scan returns callbacks, but ads from
    # the Bumble USB bridge never include the real printer — GATT then times out.
    if ($apiLevel -gt 0 -and $apiLevel -lt 31 -and -not $AllowUnsupportedApi) {
        $detail = "SKIP: emulator Bluetooth/netsim HCI requires API 31+ (see Android emulator networking docs)"
        $summary = "API=$api outcome=SKIP $detail"
        $utf8NoBom = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($resultFile, $summary + [Environment]::NewLine, $utf8NoBom)
        Write-Host $summary -ForegroundColor Yellow
        return [pscustomobject]@{ Api = $api; Outcome = "SKIP"; Detail = $detail }
    }
    if ($apiLevel -gt 0 -and $apiLevel -lt 31) {
        Write-Host "WARNING: API $api is below supported netsim HCI (31+); continuing due to -AllowUnsupportedApi" -ForegroundColor Yellow
    }

    Stop-Emulators
    Start-Sleep -Seconds 2
    # Reuse suite bridge when healthy; ForceReset only if nothing listens.
    Start-Bumble

    $emuLog = Join-Path $OutDir ("emu_api{0}.log" -f $api)
    $emuErr = Join-Path $OutDir ("emu_api{0}.err.log" -f $api)
    # Ensure-Avd may historically return Object[]; take the last string name.
    $avdName = if ($avd -is [array]) { [string]$avd[-1] } else { [string]$avd }
    $avdName = $avdName.Trim()
    if ($avdName -notmatch "^${AvdPrefix}_API") {
        throw "Invalid AVD name from Ensure-Avd: '$avdName'"
    }
    $emuArgs = @(
        "-avd", $avdName,
        "-packet-streamer-endpoint", "localhost:$BumblePort",
        "-no-snapshot-load",
        "-no-window",
        "-no-audio",
        "-no-boot-anim"
    )
    $hostZone = Get-HostZoneInfoId
    if ($hostZone) {
        $emuArgs += @("-timezone", $hostZone)
    }
    $proc = Start-Process -FilePath $Emulator `
        -ArgumentList $emuArgs `
        -RedirectStandardOutput $emuLog `
        -RedirectStandardError $emuErr `
        -PassThru -WindowStyle Hidden

    # wait for adb device
    $serial = $null
    $deadline = (Get-Date).AddSeconds(180)
    do {
        Start-Sleep -Seconds 3
        try { & $Adb start-server 2>$null | Out-Null } catch {}
        $devs = & $Adb devices 2>$null
        foreach ($line in $devs) {
            if ("$line" -match "^(emulator-\d+)\s+device") { $serial = $Matches[1]; break }
        }
    } while (-not $serial -and (Get-Date) -lt $deadline)
    if (-not $serial) { throw "Emulator did not appear in adb for API $api" }

    Wait-Boot $serial
    Sync-EmulatorClock $serial
    # Give netsim/Bumble HCI time to bind before first enable.
    Start-Sleep -Seconds 20
    $installOut = & $Adb -s $serial install --bypass-low-target-sdk-block -r $apk 2>&1 | Out-String
    if ($installOut -notmatch "Success") {
        # Older adb may not support the bypass flag.
        $installOut = & $Adb -s $serial install -r $apk 2>&1 | Out-String
    }
    if ($installOut -notmatch "Success") { throw "Install failed on API $api : $installOut" }
    Grant-Permissions $serial $apiLevel

    $extResult = "/sdcard/Android/data/$Package/files/acceptance_result.txt"
    $privResult = "files/acceptance_result.txt"
    & $Adb -s $serial shell rm -f $extResult 2>&1 | Out-Null
    & $Adb -s $serial shell run-as $Package rm -f $privResult 2>&1 | Out-Null
    & $Adb -s $serial logcat -G 16M 2>&1 | Out-Null
    & $Adb -s $serial logcat -c 2>&1 | Out-Null
    & $Adb -s $serial shell am force-stop $Package 2>&1 | Out-Null
    # Marker so we ignore any stale result that might reappear.
    $runToken = [guid]::NewGuid().ToString("N").Substring(0, 8)
    $startOut = ""
    $started = $false
    for ($i = 0; $i -lt 10; $i++) {
        $startOut = (& $Adb -s $serial shell am start -n "$Package/$Activity" --es runToken $runToken 2>&1 | ForEach-Object { "$_" }) -join "`n"
        if ($startOut -match "Starting:\s*Intent" -and $startOut -notmatch "Can't find service:\s*activity") {
            $started = $true
            break
        }
        Write-Host "am start not ready yet (try $($i+1)): $startOut"
        Start-Sleep -Seconds 5
    }
    if (-not $started) { throw "Failed to start activity: $startOut" }
    $startedAt = Get-Date

    # Wait for test completion: result file (preferred) or logcat marker.
    # Require the result to appear after start (mtime/content freshness via startedAt wait min 5s).
    Write-Host "Waiting for acceptance test to finish (timeout ${ConnectTimeoutSec}s)..."
    $outcome = "TIMEOUT"
    $detail = ""
    $deadline = (Get-Date).AddSeconds($ConnectTimeoutSec)
    do {
        Start-Sleep -Seconds 2
        # Do not accept a result within the first few seconds (guards against uncleared stale files).
        if (((Get-Date) - $startedAt).TotalSeconds -lt 5) { continue }
        $text = Get-AdbUtf8Text -AdbArgs @("-s", $serial, "shell", "cat", $extResult) -Trim
        if (-not ($text -match "ACCEPTANCE_TEST result=")) {
            $text = Get-AdbUtf8Text -AdbArgs @("-s", $serial, "shell", "run-as", $Package, "cat", $privResult) -Trim
        }
        if (-not ($text -match "ACCEPTANCE_TEST result=")) {
            $text = Get-AdbUtf8Text -AdbArgs @("-s", $serial, "logcat", "-d", "-s", "AcceptanceConnect:I")
        }
        if ($text -match "ACCEPTANCE_TEST result=") {
            $detail = ([regex]::Matches($text, "ACCEPTANCE_TEST result=\S[^\r\n]*") | Select-Object -Last 1).Value
            if ($detail -match "result=PASS") { $outcome = "PASS" } else { $outcome = "FAIL" }
            break
        }
    } while ((Get-Date) -lt $deadline)

    $summary = "API=$api serial=$serial outcome=$outcome $detail"
    $utf8NoBom = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($resultFile, $summary + [Environment]::NewLine, $utf8NoBom)
    # Dump full logcat as UTF-8 (avoid CP866 mojibake on Russian Windows).
    Start-Sleep -Seconds 2
    $fullPath = Join-Path $OutDir ("logcat_api{0}_full.txt" -f $api)
    $filtPath = Join-Path $OutDir ("logcat_api{0}.txt" -f $api)
    $all = @(Save-AdbLogcat -Serial $serial -OutPath $fullPath)
    $filtered = @($all | Where-Object {
            $_ -match "com\.shtrih|AcceptanceConnect|BluetoothLE|PPPPort|JposException|BluetoothGatt|BluetoothLeScanner|bt_hci|netsim|bumble|ACCEPTANCE_TEST|Port open|Logback"
        })
    [System.IO.File]::WriteAllLines($filtPath, $filtered, $utf8NoBom)
    Write-Host $summary -ForegroundColor $(if ($outcome -eq "PASS") { "Green" } else { "Red" })
    Write-Host ("logcat lines: full={0} filtered={1}" -f $all.Count, $filtered.Count)

    if (-not $KeepEmulators) {
        & $Adb -s $serial emu kill 2>&1 | Out-Null
        Start-Sleep -Seconds 2
        if (-not $proc.HasExited) { Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue }
    }
    return [pscustomobject]@{ Api = $api; Outcome = $outcome; Detail = $detail }
}

# ---- main ----
Write-Step "Repo=$RepoRoot Sdk=$Sdk"
# Kill anything leftover before suite starts.
Stop-Emulators
Ensure-Images
$apk = Build-Apk
Write-Host "APK=$apk"
# One HCI bridge for all API levels (restarting USB/HCI between APIs wedges the Realtek dongle).
Start-Bumble

$results = @()
foreach ($api in $ApiLevels) {
    try {
        $one = Run-One -api $api -apk $apk
        $results += $one
        if ($StopOnError -and $one.Outcome -ne "PASS" -and $one.Outcome -ne "SKIP") {
            Write-Host "StopOnError: halting suite after API $api ($($one.Outcome))" -ForegroundColor Red
            Stop-Emulators
            break
        }
    } catch {
        $msg = $_.Exception.Message
        Write-Host "API $api ERROR: $msg" -ForegroundColor Red
        Set-Content -Path (Join-Path $OutDir ("API{0}.txt" -f $api)) -Value "API=$api outcome=ERROR detail=$msg" -Encoding UTF8
        $results += [pscustomobject]@{ Api = $api; Outcome = "ERROR"; Detail = $msg }
        Stop-Emulators
        if ($StopOnError) {
            Write-Host "StopOnError: halting suite after API $api ERROR" -ForegroundColor Red
            break
        }
    }
}

Write-Step "Summary"
$results | Format-Table -AutoSize
$failed = @($results | Where-Object { $_.Outcome -ne "PASS" -and $_.Outcome -ne "SKIP" })
$summaryPath = Join-Path $OutDir "SUMMARY.txt"
$results | ForEach-Object { "$($_.Api)=$($_.Outcome) $($_.Detail)" } | Set-Content $summaryPath -Encoding UTF8
Write-Host "Results in $OutDir"
if ($failed.Count -gt 0) { exit 1 } else { exit 0 }
