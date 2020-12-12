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
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
