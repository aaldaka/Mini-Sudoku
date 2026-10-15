package sudoku.utils;

import sudoku.exception.InvalidCharacterException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SudokuApiClient {

    private static final String API_URL = "https://www.youdosudoku.com/api";
    private static final String API_KEY = System.getenv("SUDOKU_API_KEY");

    public static int[][] fetchPuzzle(String difficulty) throws Exception {
        if (API_KEY == null) {
            throw new Exception("No API key set");
        }

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        String body = "{\"difficulty\":\"" + difficulty + "\",\"solution\":false,\"array\":false}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("x-api-key", API_KEY)
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //logging
//        System.out.println("Redirects to: " + response.headers().firstValue("location").orElse("none"));
//        System.out.println("API response: " + response.body());

        if (response.statusCode() != 200) {
            throw new Exception("API returned " + response.statusCode());
        }

        String puzzle = extractPuzzle(response.body());
        return toGrid(puzzle);
    }

    // {"puzzle": "530070000600195000..."}
    private static String extractPuzzle(String json) {
        int start = json.indexOf("puzzle");
        if (start == -1) {
            throw new InvalidCharacterException("No puzzle in the API response");
        }

        int colon = json.indexOf(':', start);   // the : after the word puzzle
        int quote = json.indexOf('"', colon);   // the " where the digits start
        if (colon == -1 || quote == -1) { // missing pieces
            throw new InvalidCharacterException("Puzzle format is incomplete");
        }

        return json.substring(quote + 1, quote + 82);
    }

    // 023008... -> int[9][9]
    private static int[][] toGrid(String puzzle) {
        if (puzzle.length() != 81) {
            throw new InvalidCharacterException("Puzzle from the API is not 81 digits");
        }

        int[][] numbers = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                char c = puzzle.charAt(row * 9 + col);
                if (!Character.isDigit(c)) {
                    throw new InvalidCharacterException("Invalid character '" + c + "' from the API");
                }
                numbers[row][col] = Character.getNumericValue(c);
            }
        }
        return numbers;
    }
}