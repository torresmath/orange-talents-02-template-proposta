package com.zup.proposal.financialproposal.common;

import java.util.Base64;

public class Base64Utils {

    private Base64Utils() { }

    public static boolean isBase64(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
