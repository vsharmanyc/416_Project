package com.panthers.main.dataaccess;

import java.io.InputStream;
import java.util.Properties;

public class SeaWulfProperties {
    private int seaWulfDistrictingThreshold;
    private String transferDataBash;
    private String swDataPrefix;
    private String precinctDataSuffix;
    private String bashScript;
    private String serverStaticWd;
    private String slurmScript;
    private String slurmScriptPath;
    private String netID;
    private String password;
    private String swSummaryTransferFile;
    private String transferSummaryBash;
    private String transferResultBash;
    private String transferResultFile;

    private InputStream input;

    public SeaWulfProperties() {

    }

    public void getProperties() {
        try {
            Properties properties = new Properties();
            String config = "config.properties";

            input = getClass().getClassLoader().getResourceAsStream(config);
            if (input != null)
                properties.load(input);

            seaWulfDistrictingThreshold = Integer.parseInt(properties.getProperty("seaWulfDistrictingThreshold"));
            transferDataBash = properties.getProperty("transferDataBash");
            swDataPrefix = properties.getProperty("swDataPrefix");
            precinctDataSuffix = properties.getProperty("precinctDataSuffix");
            bashScript = properties.getProperty("bashScript");
            serverStaticWd = properties.getProperty("serverStaticWd");
            slurmScript = properties.getProperty("slurmScript");
            slurmScriptPath = properties.getProperty("slurmScriptPath");
            netID = properties.getProperty("netID");
            password = properties.getProperty("password");
            swSummaryTransferFile = properties.getProperty("swSummaryTransferFile");
            transferSummaryBash = properties.getProperty("transferSummaryBash");
            transferResultBash = properties.getProperty("transferResultBash");
            transferResultFile = properties.getProperty("transferResultFile");
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTransferResultFile() {
        return transferResultFile;
    }

    public String getTransferResultBash() {
        return transferResultBash;
    }

    public String getNetID() {
        return netID;
    }

    public String getPassword() {
        return password;
    }

    public String getSwSummaryTransferFile() {
        return swSummaryTransferFile;
    }

    public String getTransferSummaryBash() {
        return transferSummaryBash;
    }

    public InputStream getInput() {
        return input;
    }

    public String getSlurmScriptPath() {
        return slurmScriptPath;
    }

    public void setSlurmScriptPath(String slurmScriptPath) {
        this.slurmScriptPath = slurmScriptPath;
    }

    public String getSlurmScript() {
        return slurmScript;
    }

    public void setSlurmScript(String slurmScript) {
        this.slurmScript = slurmScript;
    }

    public int getSeaWulfDistrictingThreshold() {
        return seaWulfDistrictingThreshold;
    }

    public String getTransferDataBash() {
        return transferDataBash;
    }

    public String getSwDataPrefix() {
        return swDataPrefix;
    }

    public String getPrecinctDataSuffix() {
        return precinctDataSuffix;
    }

    public String getBashScript() {
        return bashScript;
    }

    public String getServerStaticWd() {
        return serverStaticWd;
    }
}
