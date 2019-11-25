package com.example.wordbook.model;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Word {
    private String word;
    private String interpretation;
    private String example;
    private Long id;

    public Word(String word, String interpretation, String example) {
        this.word = word;
        this.interpretation = interpretation;
        this.example = example;
        this.id = -1L;
    }

    public Word(long id, String word, String interpretation, String example) {
        this.word = word;
        this.interpretation = interpretation;
        this.example = example;
        this.id = id;
    }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,
        "id=%d, word='%s', interpretation='%s', example='%s'", this.id,
                this.word, this.interpretation, this.example);
    }
}
