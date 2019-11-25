package com.example.wordbook.ui.wordDetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wordbook.R;
import com.example.wordbook.model.Word;
import com.example.wordbook.model.WordLibrary;
import com.example.wordbook.ui.share.LogTag;

public class FragmentWordDetail extends Fragment {
    private View container;
    private TextView word, interpretation, example;
    private Word wordObject;

    public FragmentWordDetail(Word wordObject) {
        this.wordObject = wordObject;
        Log.i(LogTag.wordDetail,
                String.format("init the fragment, the word item is: %s", wordObject.toString()));
    }

    private void assignView() {
        this.word = container.findViewById(R.id.textView_wordDetail_word);
        this.interpretation = container.findViewById(R.id.textView_wordDetail_interpretation);
        this.example = container.findViewById(R.id.textView_wordDetail_example);
    }

    private void importContent() {
        this.word.setText(wordObject.getWord());
        this.interpretation.setText(wordObject.getInterpretation());
        this.example.setText(wordObject.getExample());
    }

    private void assignAction() {
        this.word.setClickable(true);
        this.interpretation.setClickable(true);
        this.example.setClickable(true);

        this.word.setOnClickListener((View v) -> this.showDialog(this.word.getId()));
        this.interpretation.setOnClickListener((View v) -> this.showDialog(this.interpretation.getId()));
        this.example.setOnClickListener((View v) -> this.showDialog(this.example.getId()));
    }

    private void showDialog(int sourceView) {
        Log.d(LogTag.wordDetail, "showing dialog.");

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        switch (sourceView) {
            case R.id.textView_wordDetail_word: builder.setTitle("modify the word"); break;
            case R.id.textView_wordDetail_interpretation: builder.setTitle("modify the interpretation"); break;
            case R.id.textView_wordDetail_example: builder.setTitle("modify the example"); break;
        }

        final View dialogView = View.inflate(this.getContext(), R.layout.dialog_changetext, null);
        builder.setView(dialogView);

        builder.setPositiveButton("modify", (DialogInterface dia, int which) -> {
            EditText editText =  dialogView.findViewById(R.id.editText_changeText_newContent);
            this.saveState(sourceView, editText.getText().toString());
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    private void saveState(int sourceEvent, String text) {
        switch (sourceEvent) {
            case R.id.textView_wordDetail_word: this.wordObject.setWord(text); break;
            case R.id.textView_wordDetail_interpretation: this.wordObject.setInterpretation(text); break;
            case R.id.textView_wordDetail_example: this.wordObject.setExample(text); break;
        }
        WordLibrary.getInstance(this.getContext()).updateWord(this.wordObject);
        this.importContent();

        Log.i(LogTag.wordDetail,
                String.format("update the word, the word is: %s", wordObject.toString()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.container = inflater.inflate(R.layout.fragment_worddetail, container, false);
        this.assignView();
        this.importContent();
        this.assignAction();
        return this.container;
    }
}
