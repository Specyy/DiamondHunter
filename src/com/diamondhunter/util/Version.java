package com.diamondhunter.util;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logging.Logger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class Version {

    public static final String DEFAULT_ALPHA_IDENTIFIER = "ALPHA";
    public static final String DEFAULT_BETA_IDENTIFIER = "BETA";

    private String version;

    private Map<String, Boolean> betaIdentifiers;
    private Map<String, Boolean> alphaIdentifiers;

    public Version(String version) {
        this.version = version;
        betaIdentifiers = new LinkedHashMap<>();
        alphaIdentifiers = new LinkedHashMap<>();

        alphaIdentifiers.put(DEFAULT_ALPHA_IDENTIFIER, true);
        betaIdentifiers.put(DEFAULT_BETA_IDENTIFIER, true);
    }

    public boolean isAlpha() {
        for (Map.Entry<String, Boolean> en : alphaIdentifiers.entrySet()) {
            if (en.getValue())
                if (version.toUpperCase().contains(en.getKey().toUpperCase()))
                    return true;
                else if (version.contains(en.getKey()))
                    return true;
        }

        return false;
    }

    public boolean isBeta() {
        for (Map.Entry<String, Boolean> en : betaIdentifiers.entrySet()) {
            if (en.getValue())
                if (version.toUpperCase().contains(en.getKey().toUpperCase()))
                    return true;
                else if (version.contains(en.getKey()))
                    return true;
        }

        return false;
    }

    public boolean isNumber() {
        try {
            NumberFormat.getNumberInstance().parse(version);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public boolean isDecimal() {
        return version.contains(".") && (version.lastIndexOf('.') == version.indexOf('.')) && isNumber();
    }

    public Number toNumber() {
        try {
            return NumberFormat.getNumberInstance().parse(version);
        } catch (ParseException e) {
            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not parse number: " + version);
        }

        return null;
    }

    public void addBetaIdentifier(String identifier, boolean ignoreCase) {
        betaIdentifiers.put(identifier, ignoreCase);
    }

    public void removeBetaIdentifier(String identifier) {
        betaIdentifiers.remove(identifier);
    }

    public void removeBetaIdentifier(String identifier, boolean ignoreCase) {
        betaIdentifiers.remove(identifier, ignoreCase);
    }

    public void addAlphaIdentifier(String identifier, boolean ignoreCase) {
        alphaIdentifiers.put(identifier, ignoreCase);
    }

    public void removeAlphaIdentifier(String identifier) {
        alphaIdentifiers.remove(identifier);
    }

    public void removeAlphaIdentifier(String identifier, boolean ignoreCase) {
        alphaIdentifiers.remove(identifier, ignoreCase);
    }

    public Map<String, Boolean> getAlphaIdentifiers() {
        return alphaIdentifiers;
    }

    public Map<String, Boolean> getBetaIdentifiers() {
        return betaIdentifiers;
    }

    @Override
    public String toString() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Version) {
            return version.equals(((Version) obj).version);
        } else if (obj instanceof String) {
            return version.equals(obj);
        } else if (obj instanceof Number) {
            if (!isNumber()) return false;

            return toNumber().doubleValue() == ((Number) obj).doubleValue();
        }

        return super.equals(obj);
    }
}
