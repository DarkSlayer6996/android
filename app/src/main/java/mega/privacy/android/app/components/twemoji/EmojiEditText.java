package mega.privacy.android.app.components.twemoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.CallSuper;
import android.support.annotation.DimenRes;
import android.support.annotation.Px;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.widget.EmojiAppCompatEditText;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.KeyEvent;

import mega.privacy.android.app.R;
import mega.privacy.android.app.components.twemoji.emoji.Emoji;
import mega.privacy.android.app.utils.Util;

//Reference implementation for an EditText with emoji support
public class EmojiEditText extends AppCompatEditText implements EmojiEditTextInterface {
    private float emojiSize;
    private boolean isInput = false;
    public EmojiEditText(final Context context) {
        this(context, null);
    }
    public EmojiEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            EmojiManager.getInstance().verifyInstalled();
        }
        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
        if (attrs == null) {
            emojiSize = defaultEmojiSize;
        } else {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EmojiEditText);
            try {
                emojiSize = a.getDimension(R.styleable.EmojiEditText_emojiSize, defaultEmojiSize);
            } finally {
                a.recycle();
            }
        }

        setText(getText());
    }
    @Override public void setText(CharSequence rawText, BufferType type) {
        isInput = false;

        super.setText(rawText, type);
    }

    @Override @CallSuper protected void onTextChanged(CharSequence rawText, int start, int lengthBefore, int lengthAfter) {
//
        if(isInput){
        }else{
            CharSequence textEmojiCompat = EmojiCompat.get().process(getText());
        }
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;

        EmojiManager.getInstance().replaceWithImages(getContext(), getText(), emojiSize, defaultEmojiSize);
    }

    @Override @CallSuper public void input(Emoji emoji) {
        if (emoji != null) {
            isInput = true;
            final int start = getSelectionStart();
            final int end = getSelectionEnd();
            if (start < 0) {
                append(emoji.getUnicode());
            } else {
                getText().replace(Math.min(start, end), Math.max(start, end), emoji.getUnicode(), 0, emoji.getUnicode().length());
            }
        }else{
        }
    }

//    @Override @CallSuper protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
//        final Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
//        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;//
//        EmojiManager.getInstance().replaceWithImages(getContext(), getText(), emojiSize, defaultEmojiSize);
//    }

    @Override public float getEmojiSize() {
        return emojiSize;
    }
    @Override @CallSuper public void backspace() {
        final KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        dispatchKeyEvent(event);
    }

    @Override public final void setEmojiSize(@Px final int pixels) {
        setEmojiSize(pixels, true);
    }
    @Override public final void setEmojiSize(@Px final int pixels, final boolean shouldInvalidate) {
        emojiSize = pixels;
        if (shouldInvalidate) {
            setText(getText());
        }
    }
    @Override public final void setEmojiSizeRes(@DimenRes final int res) {
        setEmojiSizeRes(res, true);
    }
    @Override public final void setEmojiSizeRes(@DimenRes final int res, final boolean shouldInvalidate) {
        setEmojiSize(getResources().getDimensionPixelSize(res), shouldInvalidate);
    }
    public static void log(String message) {
        Util.log("EmojiEditText", message);
    }
}