package chapter5;

import chapter2.Chapter2;
import common.Log;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by codemaker88 on 2018-03-03.
 */

public class Chapter5 {
    private final String LOTTO_URL = "http://www.nlotto.co.kr/common.do?method=getLottoNumber";
    private final String TURN_PARAM = "&drwNo=";
    private final String NUMBER = "drwtNo";
    private final String BONUS_NUMBER = "bnusNo";
    private final String WINNER_COUNT_PARAM = "firstPrzwnerCo";

    private final String SAVE_FILE = "save";
    private final String REV_FILE = "rev";

    public void solve() {
        Study3.Lotto lotto = new Study3.Lotto();
        final int currentTurn = lotto.getCurrentTurnNumber();
        int savedTurn = 0;

        if (Files.exists(Paths.get(REV_FILE))) {
            try {
                savedTurn = Integer.parseInt(Files.readAllLines(Paths.get(REV_FILE)).get(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (currentTurn - savedTurn > 0) {
            Observable<String> fileSaveObservable = Observable.range(savedTurn + 1, currentTurn - savedTurn)
                    //.subscribeOn(Schedulers.io())
                    //.observeOn(Schedulers.io())
                    .map(turnIndex -> LOTTO_URL + TURN_PARAM + turnIndex)
                    .map(OkHttpHelper::get);

            fileSaveObservable.subscribe(
                    s -> Files.write(Paths.get(SAVE_FILE), String.format("%s\n", s).getBytes(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND),
                    Throwable::printStackTrace);

            try {
                Files.write(Paths.get(REV_FILE), Integer.toString(currentTurn).getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> savedLotto = null;
        try {
            savedLotto = Files.readAllLines(Paths.get(SAVE_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedLotto != null) {
            Map<Integer, Integer> countMap = new HashMap<>();
            for (int i = 1; i <= 45; i++) {
                countMap.put(i, 0);
            }

            Observable<String> parsingObservable = Observable.fromIterable(savedLotto)
                    .subscribeOn(Schedulers.trampoline());

            parsingObservable.forEach(s -> {
                int winnerCount = Integer.parseInt(parseInteger(s, WINNER_COUNT_PARAM));

                if (winnerCount == 0) {
                    winnerCount = 1;
                }

                for (int j = 1; j <= 6; j++) {
                    int num = Integer.parseInt(parseInteger(s, NUMBER + j));
                    countMap.put(num, countMap.get(num) + winnerCount);
                }
                int num7 = Integer.parseInt(parseInteger(s, BONUS_NUMBER));
                countMap.put(num7, countMap.get(num7) + winnerCount);
            });

            //2장 ObservableForMap 으로 정렬 활용
            Observable<Map.Entry<Integer, Integer>> result
                    = Chapter2.ObservableForMap.fromMap(Map.Entry.comparingByValue(Comparator.reverseOrder()), countMap)
                    .take(7);

            result.subscribe(
                    System.out::println,
                    Throwable::printStackTrace,
                    () -> System.out.println("onComplete"));
        }

    }

    private String parseInteger(String json, String target) {
        return parse(json, String.format("\"%s\":[0-9]*", target)).split(":")[1];
    }

    private String parse(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(json);
        if (match.find()) {
            return match.group();
        }
        return "N/A";
    }

    public static class OkHttpHelper {
        private static OkHttpClient client = new OkHttpClient();
        public static String ERROR = "ERROR";

        public static String get(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response res = client.newCall(request).execute();
                return res.body().string();
            } catch (IOException e) {
                Log.e(e.getMessage());
                throw e;
            }
        }

        public static String getT(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response res = client.newCall(request).execute();
                return res.body().string();
            } catch (IOException e) {
                Log.et(e.getMessage());
                throw e;
            }
        }

        public static String getWithLog(String url) throws IOException {
            Log.d("OkHttp call URL = " + url);
            return get(url);
        }
    }
}
