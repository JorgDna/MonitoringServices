/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.util;

import java.io.StringReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 *
 * @author usuario
 */
public class Utils {

    public static String hexToBinary(String hex) {
        String binaryData = new BigInteger(hex, 16).toString(2);
        StringBuilder builder = new StringBuilder(binaryData);
        builder.reverse();
        if (binaryData.length() < 8) {
            while (builder.length() < 8) {
                builder.append(0);
            }
        }
        return builder.reverse().toString();
    }

    public static Double processStandardLatitudeFromHexValue(String hex) {
        int decimalValue = Integer.parseInt(hex, 16);
        double value = decimalValue * (90.0 / Math.pow(2, 23));
        if (value > 90.0) {
            value = value - 180.0;
        }
        return value;
    }

    public static Double processSolarLatitudeFromHexValue(String hex) {
        int decimalValue = Integer.parseInt(hex, 16);
        double value = decimalValue * (90.0 / Math.pow(2, 23));
        if (value > 90.0) {
            value = value - 180.0;
        }
        return value;
    }

    public static Double processStandardLongitudeFromHexValue(String hex) {
        int decimalValue = Integer.parseInt(hex, 16);
        double value = decimalValue * (180.0 / Math.pow(2, 23));
        if (value > 180.0) {
            value = value - 360.0;
        }
        return value;
    }

    public static Double processSolarLongitudeFromHexValue(String hex) {
        int decimalValue = Integer.parseInt(hex, 16);
        double value = decimalValue * (180.0 / Math.pow(2, 23));
        if (value > 180.0) {
            value = value - 360.0;
        }
        return value;
    }

    public static double decodeHexToDouble(String hex) {
        Long data = null;
        data = Long.parseLong(hex, 16);
        if (data != null) {
            return data.doubleValue();
        }
        return 0.0;
    }

    public static boolean isNormalStandardMessage(String binary) {
        if (binary != null && binary.length() > 2) {
            StringBuilder bin = new StringBuilder(binary);
            String revBin = bin.reverse().toString();
            return !(revBin.substring(0, 1).equals("1"));
        }
        return false;
    }

    public static boolean isPanicMessage(String binary) {
        if (binary != null && binary.length() > 2) {
            StringBuilder bin = new StringBuilder(binary);
            String revBin = bin.reverse().toString();
            return revBin.substring(1, 2).equals("0");
        }
        return false;
    }

    public static int evaluateHeadingForSolarUnits(String bin) {
        if (bin != null) {
            switch (bin) {
                case "000": {
                    return 0; // "N"
                }
                case "001": {
                    return 45; // "NE"
                }
                case "010": {
                    return 90; // "E"
                }
                case "011": {
                    return 135; // "SE"
                }
                case "100": {
                    return 180; // "S"
                }
                case "101": {
                    return 225; // "SW"
                }
                case "110": {
                    return 270; // "W"
                }
                case "111": {
                    return 315; // "NW"
                }
            }
        }
        return 0;
    }

    public static String encodeURL(String urlToEncode) {
        try {
            URL url = new URL(urlToEncode);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            return url.toString();
        } catch (Exception ex) {

        }
        return urlToEncode;
    }

    public static Date convertDateFromString(String dateInString, DateFormat dateParser) {
        try {
            return dateParser.parse(dateInString);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Document convertStringToXMLDocument(String stringXML) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = null;
            if (stringXML != null && !stringXML.isEmpty()) {
                doc = builder.parse(new InputSource(new StringReader(stringXML)));
                if (doc != null) {
                    doc.getDocumentElement().normalize();
                }
            }
            return doc;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static String cleanXMLResponse(String response) {
        response = response.replaceAll("\r", "");
        response = response.replaceAll("\n", "");
        response = response.replaceAll("\t", "");
        response = response.replaceAll("> +<", "><");
        return response;
    }

    public static String fetchTagValue(Element plateTag, String tagName) {
        try {
            return ((Element) plateTag.getElementsByTagName(tagName).item(0)).getTextContent();
        } catch (Exception ex) {
            return null;
        }
    }

    public static Double convertDoubleFromString(String doubleInString) {
        try {
            return Double.parseDouble(doubleInString);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String transformHeadingFromSpanishToEnglishAbbreviations(String headingInSpanish) {
        if (headingInSpanish != null) {
            switch (headingInSpanish.toLowerCase()) {
                case "nor-occidente":
                    return "NW";
                case "nor-oriente":
                    return "NE";
                case "norte":
                    return "N";
                case "occidente":
                    return "W";
                case "oriente":
                    return "E";
                case "sur":
                    return "S";
                case "sur-occidente":
                    return "SW";
                case "sur-oriente":
                    return "SE";
            }
        }
        return null;
    }

    public static String transformHeadingFromGradesToSpanishAbbreviations(int heading) {
        if (heading > 337 || heading <= 22) {
            return "norte";
        } else if (heading > 22 && heading <= 67) {
            return "nor-oriente";
        } else if (heading > 67 && heading <= 112) {
            return "oriente";
        } else if (heading > 112 && heading <= 157) {
            return "sur-oriente";
        } else if (heading > 157 && heading <= 202) {
            return "sur";
        } else if (heading > 202 && heading <= 247) {
            return "sur-occidente";
        } else if (heading > 247 && heading <= 292) {
            return "occidente";
        } else if (heading > 292 && heading <= 337) {
            return "nor-occidente";
        } else {
            return null;
        }
    }

    public static String removeCDATAAttributeFromString(String stringWithCDATAAttribute) {
        if (stringWithCDATAAttribute != null) {
            String result = stringWithCDATAAttribute.replaceAll("<!\\[CDATA\\[", "");
            return result.replaceAll("]]>", "");
        } else {
            return null;
        }
    }

    public static Integer convertIntegerFromString(String integerInString) {
        try {
            return Integer.parseInt(integerInString);
        } catch (Exception ex) {
            return null;
        }
    }
}
