#!/usr/bin/env python3
"""HCI bridge: Android emulator (android-netsim controller) <-> USB Realtek dongle.

Plain bumble-hci-bridge does NOT reload Realtek firmware after HCI_RESET.
Android's Bluetooth stack always sends HCI_RESET on enable; without a firmware
reload the chip keeps answering HCI commands but stops delivering LE ads / ACL.

This bridge:
  1) Opens the USB dongle with driver=rtk and loads firmware once via Host.reset()
  2) Short-circuits subsequent HCI_RESET from Android (keeps firmware alive)
  3) Forwards all other HCI traffic both ways
"""

from __future__ import annotations

import asyncio
import os
import sys

sys.stdout.reconfigure(encoding="utf-8", errors="replace")
sys.stderr.reconfigure(encoding="utf-8", errors="replace")

import bumble.logging
from bumble import hci, transport
from bumble.bridge import HCI_Bridge
from bumble.host import Host

DEFAULT_NETSIM = "android-netsim:_:8555,mode=controller"
DEFAULT_USB = "usb:[driver=rtk]0"


async def load_rtk_firmware(controller_source, controller_sink) -> Host:
    host = Host(controller_source, controller_sink)
    # Force RTK even if USB VID/PID is not in Bumble's known list (e.g. Mercusys 2C4E:0115).
    meta = dict(getattr(controller_source, "hci_metadata", None) or {})
    meta["driver"] = "rtk"
    host.hci_metadata = meta
    print(">>> loading Realtek firmware (Host.reset)...", flush=True)
    await host.reset()
    print(">>> Realtek firmware ready", flush=True)
    # Detach Host so the bridge can own the controller endpoints.
    controller_source.set_packet_sink(None)
    return host


def make_reset_short_circuit():
    reset_opcode = hci.HCI_RESET_COMMAND

    def host_to_controller_filter(hci_packet):
        if (
            hci_packet.hci_packet_type == hci.HCI_COMMAND_PACKET
            and hci_packet.op_code == reset_opcode
        ):
            # Ack RESET to Android without wiping Realtek firmware on the dongle.
            response = hci.HCI_Command_Complete_Event(
                num_hci_command_packets=1,
                command_opcode=reset_opcode,
                return_parameters=hci.HCI_StatusReturnParameters(
                    status=hci.HCI_ErrorCode.SUCCESS
                ),
            )
            print(">>> short-circuit HCI_RESET (keep RTK firmware)", flush=True)
            return (bytes(response), True)
        return None

    return host_to_controller_filter


async def async_main(netsim_spec: str, usb_spec: str) -> None:
    print(f">>> controller USB: {usb_spec}", flush=True)
    async with await transport.open_transport(usb_spec) as (c_src, c_sink):
        await load_rtk_firmware(c_src, c_sink)

        print(f">>> host netsim: {netsim_spec}", flush=True)
        async with await transport.open_transport(netsim_spec) as (h_src, h_sink):
            print(">>> bridging (RESET short-circuited)", flush=True)
            _ = HCI_Bridge(
                h_src,
                h_sink,
                c_src,
                c_sink,
                make_reset_short_circuit(),
                None,
            )
            await asyncio.get_running_loop().create_future()


def main() -> None:
    bumble.logging.setup_basic_logging(
        default_level=os.environ.get("BUMBLE_LOGLEVEL", "INFO")
    )
    netsim = sys.argv[1] if len(sys.argv) > 1 else DEFAULT_NETSIM
    usb = sys.argv[2] if len(sys.argv) > 2 else DEFAULT_USB
    if "BUMBLE_RTK_FIRMWARE_DIR" not in os.environ:
        default_fw = os.path.join(
            os.environ.get("LOCALAPPDATA", ""), "google", "bumble", "firmware", "realtek"
        )
        if os.path.isdir(default_fw):
            os.environ["BUMBLE_RTK_FIRMWARE_DIR"] = default_fw
    try:
        asyncio.run(async_main(netsim, usb))
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()
