package nonogrampkg;

import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;

public class DocumentSizeFilter extends DocumentFilter {
    int maxLen;
    public DocumentSizeFilter(int max_chars){
        maxLen = max_chars;
    }

    public void insertString(FilterBypass fb, int offset, String str, AttributeSet a) throws BadLocationException{
        if((fb.getDocument().getLength() + str.length()) <= maxLen){
            super.insertString(fb, offset, str, a);
        }
    }

    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet a) throws BadLocationException{
        if((fb.getDocument().getLength() + str.length() - length) <= maxLen){
            super.replace(fb, offset, length, str, a);
        }
    }
}
