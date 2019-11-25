package com.example.wordbook.ui.wordAdd;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.wordbook.R;
import com.example.wordbook.model.Word;
import com.example.wordbook.model.WordLibrary;
import com.example.wordbook.ui.share.LogTag;

public class FragmentWordAdd extends Fragment {

    private View container;
    private TextView word, interpretation, example;
    private Button save;

    private void saveWord() {
        Log.d(LogTag.wordAdd, "saving word.");
        Word w = new Word(
                this.word.getText().toString(),
                this.interpretation.getText().toString(),
                this.example.getText().toString());
        WordLibrary.getInstance(this.getContext()).insertWord(w);

        Log.i(LogTag.wordAdd, String.format("saved the word, %s", w.toString()));
    }

    private void assignAction() {
        Log.d(LogTag.wordAdd, "assigning action.");
        this.save.setOnClickListener((View v) -> {
            this.saveWord();
            FragmentActivity activity = this.getActivity();
            if (activity != null) {
                Log.d(LogTag.wordAdd, "pop back stack");
                activity.getSupportFragmentManager().popBackStack();
            } else {
                Log.e(LogTag.wordAdd, "cannot get activity, didn't pop back stack.");
            }
        });
    }

    private void assignView() {
        Log.d(LogTag.wordAdd, "assigning views.");
        this.word = this.container.findViewById(R.id.editText_wordAdd_word);
        this.interpretation = this.container.findViewById(R.id.editText_wordAdd_interpretation);
        this.example = this.container.findViewById(R.id.editText_wordAdd_example);
        this.save = this.container.findViewById(R.id.button_wordadd_save);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.container = inflater.inflate(R.layout.fragment_wordadd, container, false);
        this.assignView();
        this.assignAction();

        return this.container;
    }
}
