package com.example.wordbook.ui.wordlist;

import com.example.wordbook.R;
import com.example.wordbook.model.Word;
import com.example.wordbook.model.WordLibrary;
import com.example.wordbook.ui.MainActivity;
import com.example.wordbook.ui.wordDetail.FragmentWordDetail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private Fragment fragment;
    private List<Word> words;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView word, interpretation;
        Button crossButton;

        ViewHolder(View view) {
            super(view);
            this.word = view.findViewById(R.id.textView_wordList_item_word);
            this.interpretation = view.findViewById(R.id.textView_wordList_item_interpretation);
            this.crossButton = view.findViewById(R.id.button_cross);

            this.crossButton.setOnClickListener((View v) -> removeAt(getAdapterPosition()));
            this.word.setOnClickListener((View v) -> gotoDetail(getAdapterPosition()));

        }
    }

    WordListAdapter(Fragment fragment, List<Word> words) {
        this.fragment = fragment;
        this.words = words;
    }

    private void removeAt(int position) {
        Word w = this.words.get(position);
        WordLibrary.getInstance(this.fragment.getContext()).deleteWord(w.getId());
        this.words.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.words.size());
    }

    private void gotoDetail(int position) {
        MainActivity mainActivity = (MainActivity)fragment.getActivity();
        FragmentWordDetail fragmentWordDetail = new FragmentWordDetail(words.get(position));

        if (mainActivity != null)
            mainActivity.replaceFragment(fragmentWordDetail);
        else
            Log.e(this.getClass().getName(),
                "cannot get mainActivity, didn't start detail fragment.");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wordlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.interpretation.setText(words.get(position).getInterpretation());
        holder.word.setText(words.get(position).getWord());
    }

    @Override
    public int getItemCount() {
        return this.words.size();
    }
}
