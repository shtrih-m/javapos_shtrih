program SMClient;

uses
  // VCL
  SysUtils, DateUtils,
  // Indy
  IdTCPClient;

{$R *.res}

function GetParam(const S: string; N: Integer): string;
var
  i: Integer;
  Tocken: string;
  WasSeparator: Boolean;
  TockenNumber: Integer;
begin
  Result := '';
  Tocken := '';
  TockenNumber := 1;
  WasSeparator := False;
  for i := 1 to Length(S) do
  begin
    if S[i] = ',' then
    begin
      if TockenNumber = N then Break;
      WasSeparator := True;
    end else
    begin
      if WasSeparator then
      begin
        Tocken := '';
        Inc(TockenNumber);
        WasSeparator := False;
      end;
      Tocken := Tocken + S[i];
    end;
  end;
  Result := Tocken;
end;

// 1448450242,20/01/14,12:15
function SetUnixTime(const Data: string): string;
var
  D: TDateTime;
  Date: string;
  Time: string;
  Serial: string;
  Hour, Minute: Word;
  Year, Month, Day: Word;
  UnixTime: string;
begin
  Result := Data;
  try
    Serial := GetParam(Data, 1);
    Date := GetParam(Data, 2);
    Time := GetParam(Data, 3);
    if Date = '' then Exit;
    if Time = '' then Exit;

    Day := StrToInt(Copy(Date, 1, 2));
    Month := StrToInt(Copy(Date, 4, 2));
    Year := StrToInt(Copy(Date, 7, 2)) + 2000;
    Hour := StrToInt(Copy(Time, 1, 2));
    Minute := StrToInt(Copy(Time, 4, 2));
    D := EncodeDate(Year, Month, Day) + EncodeTime(Hour, Minute, 0, 0);
    UnixTime := IntToStr(DateTimeToUnix(D));
    Result := Format('%s,%s', [Serial, UnixTime]);
  except

  end;
end;

var
  i: Integer;
  Param: string;
  Answer: string;
  Request: string;
  Timeout: Integer;
  IsUnixTime: Boolean;
  Connection: TIdTCPClient;
begin
  if ParamCount < 3 then
  begin
    Writeln('');
    Writeln('Monitoring client, SHTRIH-M, MOSCOW, 2014');
    Writeln('Usage: SMClient.exe inet_addr inet_port request [timeout]');
    Writeln('');
    Writeln('  inet_addr    defines IP address');
    Writeln('  inet_port    defines port number');
    Writeln('  request      defines request name');
    Writeln('  [parameters] defines request parameters');
    Writeln('  [timeout]    defines timeout to connect and read, default 3000 ms');
    Writeln('');
    Writeln('  Supported requests: ');
    Writeln('');
    Writeln('  STATUS: request fiscal printer status');
    Writeln('          OK or error message');
    Writeln('  INFO: request fiscal printer (FP) status');
    Writeln('        FM model name, FP serial number, EJM serial number');
    Writeln('  ECTP: request electronic journal module (EJM) status');
    Writeln('        EJM serial number,EJM activation date, EJM activation time');
    Exit;
  end;

  Request := '';
  Timeout := 3000;
  IsUnixTime := False;
  Request := ParamStr(3);
  if (Request = 'OPER_REG') or (Request = 'CASH_REG') then begin
    for i := 4 to ParamCount do
    begin
      Request := Request + ' ' + Trim(ParamStr(i));
    end;
  end
  else begin
    for i := 4 to ParamCount do
    begin
      Param := Trim(ParamStr(i));
      if AnsiCompareText(Param, 'unixtime') = 0 then
      begin
        IsUnixTime := True;
      end else
      begin
        Timeout := StrToInt(Trim(Copy(Param, 3, Length(Param))));
      end;
    end;
  end;

  Connection := TIdTCPClient.Create(nil);
  try
    Connection.Host := ParamStr(1);
    Connection.Port := StrToInt(ParamStr(2));
    Connection.ReadTimeout := Timeout;
    if not Connection.Connected then
    begin
      Connection.Connect(Timeout);
    end;
    Connection.WriteLn(Request);
    Answer := Trim(Connection.ReadLn());
    if (Request = 'ECTP') and IsUnixTime then
    begin
      Answer := SetUnixTime(Answer);
    end;
    WriteLn(AnsiToUtf8(Answer));
    Connection.Disconnect;
  except
    On E: Exception do
    begin
      WriteLn('ERROR: ' + E.Message);
    end;
  end;

(*

Можно попросить ещё доработок для SMClient?
1. Нужно чтобы кодировка вывода была UTF8.
2. SMClient 127.0.0.1 50000 ECTP выводит

1448450242,20/01/14,12:15
Можно добавить параметр, например, unixtime,
т.е. «SMClient 127.0.0.1 50000 ECTP unixtime»
И он выводил бы дату время в UNIXTIME без другой информации –
для данного примера датавремя 20/01/14,12:15 в unixtime 1421745300
Надо не забыть учитывать текущую таймзону.

# date -d @1421745300
Tue Jan 20 12:15:00 MSK 2015

*)

end.


