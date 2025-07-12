package elpais.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequentWordCounter {

    public Map<String, Integer> getWordsRepeatingMoreThanTwice(List<String> headers){
        Map<String, Integer> wordCountMap = new HashMap<>();

        for (String header : headers) {
            for (String word : header.split(" ")) {
                Integer existingCount = wordCountMap.getOrDefault(word, 0);
                wordCountMap.put(word, existingCount + 1);
            }
        }

        Map<String, Integer> wordsOccurringMoreThanTwice = new HashMap<>();
        for (String word : wordCountMap.keySet()) {
            Integer existingCount = wordCountMap.get(word);
            if (existingCount > 2){
                wordsOccurringMoreThanTwice.put(word, existingCount);
            }
        }

        return wordsOccurringMoreThanTwice;
    }

}
