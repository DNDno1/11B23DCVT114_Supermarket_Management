package util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * HTML escaping and Vietnamese currency/date formatting utilities for JSP views.
 */
public final class HtmlUtil {
    private HtmlUtil() {
    }

    public static String esc(Object value) {
        if (value == null) {
            return "";
        }
        String s = String.valueOf(value);
        StringBuilder out = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&':
                    out.append("&amp;");
                    break;
                case '<':
                    out.append("&lt;");
                    break;
                case '>':
                    out.append("&gt;");
                    break;
                case '"':
                    out.append("&quot;");
                    break;
                case '\'':
                    out.append("&#x27;");
                    break;
                default:
                    out.append(c);
            }
        }
        return out.toString();
    }

    public static String money(long amount) {
        NumberFormat nf = NumberFormat.getInstance(Locale.of("vi", "VN"));
        return nf.format(amount) + " đ";
    }

    public static String itemCode(int id) {
        return String.format("SP-%03d", id);
    }

    public static String invoiceCode(int id) {
        return String.format("IVC-%03d", id);
    }
}
