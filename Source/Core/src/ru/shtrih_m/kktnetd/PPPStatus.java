package ru.shtrih_m.kktnetd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class PPPStatus
{
    /*
     * Values for phase.
     */
    public static final int PPP_PHASE_DEAD           = 0;
    public static final int PPP_PHASE_MASTER         = 1;
    public static final int PPP_PHASE_HOLDOFF        = 2;
    public static final int PPP_PHASE_INITIALIZE     = 3;
    public static final int PPP_PHASE_SERIALCONN     = 4;
    public static final int PPP_PHASE_DORMANT        = 5;
    public static final int PPP_PHASE_ESTABLISH      = 6;
    public static final int PPP_PHASE_AUTHENTICATE   = 7;
    public static final int PPP_PHASE_CALLBACK       = 8;
    public static final int PPP_PHASE_NETWORK        = 9;
    public static final int PPP_PHASE_RUNNING        = 10;
    public static final int PPP_PHASE_TERMINATE      = 11;
    public static final int PPP_PHASE_DISCONNECT     = 12;
    /* Error codes. */
    public static final int PPPERR_NONE             = 0;  /* No error. */
    public static final int PPPERR_PARAM            = 1;  /* Invalid parameter. */
    public static final int PPPERR_OPEN             = 2;  /* Unable to open PPP session. */
    public static final int PPPERR_DEVICE           = 3;  /* Invalid I/O device for PPP. */
    public static final int PPPERR_ALLOC            = 4;  /* Unable to allocate resources. */
    public static final int PPPERR_USER             = 5;  /* User interrupt. */
    public static final int PPPERR_CONNECT          = 6;  /* Connection lost. */
    public static final int PPPERR_AUTHFAIL         = 7;  /* Failed authentication challenge. */
    public static final int PPPERR_PROTOCOL         = 8;  /* Failed to meet protocol. */
    public static final int PPPERR_PEERDEAD         = 9;  /* Connection timeout */
    public static final int PPPERR_IDLETIMEOUT      = 10; /* Idle Timeout */
    public static final int PPPERR_CONNECTTIME      = 11; /* Max connect time reached */
    public static final int PPPERR_LOOPBACK         = 12; /* Loopback detected */

    public static int textToPhase(String text) {
        HashMap<String, Integer> phase = new HashMap();
        phase.put("PPP_PHASE_DEAD", PPP_PHASE_DEAD);
        phase.put("PPP_PHASE_MASTER", PPP_PHASE_MASTER);
        phase.put("PPP_PHASE_HOLDOFF", PPP_PHASE_HOLDOFF);
        phase.put("PPP_PHASE_INITIALIZE", PPP_PHASE_INITIALIZE);
        phase.put("PPP_PHASE_SERIALCONN", PPP_PHASE_SERIALCONN);
        phase.put("PPP_PHASE_DORMANT", PPP_PHASE_DORMANT);
        phase.put("PPP_PHASE_ESTABLISH", PPP_PHASE_ESTABLISH);
        phase.put("PPP_PHASE_AUTHENTICATE", PPP_PHASE_AUTHENTICATE);
        phase.put("PPP_PHASE_CALLBACK", PPP_PHASE_CALLBACK);
        phase.put("PPP_PHASE_NETWORK", PPP_PHASE_NETWORK);
        phase.put("PPP_PHASE_RUNNING", PPP_PHASE_RUNNING);
        phase.put("PPP_PHASE_TERMINATE", PPP_PHASE_TERMINATE);
        phase.put("PPP_PHASE_DISCONNECT", PPP_PHASE_DISCONNECT);
        return phase.get(text);
    }

    public static int textToError(String text) {
        HashMap<String, Integer> error = new HashMap();
        error.put("PPPERR_NONE", PPPERR_NONE);
        error.put("PPPERR_PARAM", PPPERR_PARAM);
        error.put("PPPERR_OPEN", PPPERR_OPEN);
        error.put("PPPERR_DEVICE", PPPERR_DEVICE);
        error.put("PPPERR_ALLOC", PPPERR_ALLOC);
        error.put("PPPERR_USER", PPPERR_USER);
        error.put("PPPERR_CONNECT", PPPERR_CONNECT);
        error.put("PPPERR_AUTHFAIL", PPPERR_AUTHFAIL);
        error.put("PPPERR_PROTOCOL", PPPERR_PROTOCOL);
        error.put("PPPERR_PEERDEAD", PPPERR_PEERDEAD);
        error.put("PPPERR_IDLETIMEOUT", PPPERR_IDLETIMEOUT);
        error.put("PPPERR_CONNECTTIME", PPPERR_CONNECTTIME);
        error.put("PPPERR_LOOPBACK", PPPERR_LOOPBACK);
        return error.get(text);
    }

    public String phase = "";
    public String status = "";

    public PPPStatus() {
    }

    public PPPStatus(String phase, String status) {
        this.phase = phase;
        this.status = status;
    }

    public static PPPStatus fromJson(String jsonText) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return (PPPStatus) gson.fromJson(jsonText, PPPStatus.class);
    }

    public boolean isEqual(PPPStatus status){
        return (this.phase.equalsIgnoreCase(status.phase) &&
                (this.status.equalsIgnoreCase(status.status)));
    }
}