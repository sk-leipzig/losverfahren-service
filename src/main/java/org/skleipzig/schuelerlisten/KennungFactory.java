package org.skleipzig.schuelerlisten;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class KennungFactory {
    private static final Random RANDOM = new Random();
    private Set<String> kennungen = new HashSet<>();

    public String createKennung(int losverfahrenId, int length) {
        StringBuffer strBuf = new StringBuffer(Integer.toString(losverfahrenId));
        for (int i = strBuf.length(); i < length; i++) {
            strBuf.append(Integer.toString(RANDOM.nextInt(9)));
        }
        String kennung = strBuf.toString();
        return kennungen.add(kennung) ? kennung : createKennung(losverfahrenId, length); // eindeutig!
    }

    public void addExisting(String kennung) {
        if (!kennungen.add(kennung))
            throw new IllegalStateException("Doppelte Kennung: " + kennung);
    }
}
