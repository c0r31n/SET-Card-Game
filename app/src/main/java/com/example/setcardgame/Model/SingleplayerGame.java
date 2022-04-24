package com.example.setcardgame.Model;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.setcardgame.Model.card.Card;

import java.util.ArrayList;

public class SingleplayerGame{

    public SingleplayerGame() {}

    public boolean hasSet(ArrayList<Card> cards, ArrayList<ImageView> board) {
        if (cards.size() >= 3) {
            ArrayList<Boolean> propertyChecks = new ArrayList<>();
            for (int i = 0; 3 > i; i++) propertyChecks.add(false);

            for (int i = 0; cards.size() > i; i++) {
                for (int j = i + 1; cards.size() > j; j++) {
                    for (int k = j + 1; cards.size() > k; k++) {
                        for (int x = 0; 3 > x; x++) propertyChecks.set(x, false);
                        if (cards.get(i).getColor() == cards.get(j).getColor() && cards.get(i).getColor() == cards.get(k).getColor())
                            propertyChecks.set(0, true);
                        if (cards.get(i).getColor() != cards.get(j).getColor() && cards.get(i).getColor() != cards.get(k).getColor() && cards.get(j).getColor() != cards.get(k).getColor())
                            propertyChecks.set(0, true);
                        if (cards.get(i).getShape() == cards.get(j).getShape() && cards.get(i).getShape() == cards.get(k).getShape())
                            propertyChecks.set(1, true);
                        if (cards.get(i).getShape() != cards.get(j).getShape() && cards.get(i).getShape() != cards.get(k).getShape() && cards.get(j).getShape() != cards.get(k).getShape())
                            propertyChecks.set(1, true);
                        if (cards.get(i).getQuantity() == cards.get(j).getQuantity() && cards.get(i).getQuantity() == cards.get(k).getQuantity())
                            propertyChecks.set(2, true);
                        if (cards.get(i).getQuantity() != cards.get(j).getQuantity() && cards.get(i).getQuantity() != cards.get(k).getQuantity() && cards.get(j).getQuantity() != cards.get(k).getQuantity())
                            propertyChecks.set(2, true);

                        if (!propertyChecks.contains(false)) {
                            ArrayList<Boolean> visibilityChecks = new ArrayList<>();
                            for (int z = 0; 3 > z; z++) visibilityChecks.add(false);

                            String cardDesc1 = cards.get(i).toString();
                            String cardDesc2 = cards.get(j).toString();
                            String cardDesc3 = cards.get(k).toString();

                            for (int y = 0; board.size() > y; y++) {
                                String boardDesc = (String) board.get(y).getContentDescription();

                                if (boardDesc.equals(cardDesc1)) {
                                    if (board.get(y).getVisibility() == View.VISIBLE) {
                                        visibilityChecks.set(0, true);
                                    }
                                }
                                if (boardDesc.equals(cardDesc2)) {
                                    if (board.get(y).getVisibility() == View.VISIBLE) {
                                        visibilityChecks.set(1, true);
                                    }
                                }
                                if (boardDesc.equals(cardDesc3)) {
                                    if (board.get(y).getVisibility() == View.VISIBLE) {
                                        visibilityChecks.set(2, true);
                                    }
                                }
                            }

                            if (!visibilityChecks.contains(false)) {
                                Log.d("cheat", "card1: " + cardDesc1);
                                Log.d("cheat", "card2: " + cardDesc2);
                                Log.d("cheat", "card3: " + cardDesc3);
                                Log.d("cheat", " ");
                                propertyChecks.clear();
                                visibilityChecks.clear();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isGameOver(ArrayList<ImageView> board) {
        boolean hasVisible = false;

        for (int i = 0; board.size() > i && !hasVisible; i++) {
            if (board.get(i).getVisibility() == View.VISIBLE) {
                hasVisible = true;
            }
        }
        return !hasVisible;
    }
}

