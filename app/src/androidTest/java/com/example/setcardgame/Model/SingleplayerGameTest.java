package com.example.setcardgame.Model;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;

import com.example.setcardgame.Model.card.Card;
import com.example.setcardgame.Model.card.Color;
import com.example.setcardgame.Model.card.Quantity;
import com.example.setcardgame.Model.card.Shape;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SingleplayerGameTest {
    Context c = ApplicationProvider.getApplicationContext();
    ArrayList<ImageView> board = new ArrayList<>();
    ArrayList<Card> cards = new ArrayList<>();
    SingleplayerGame game = new SingleplayerGame();

    @Before
    public void setUp() {
        cards.clear();
        board.clear();

        cards.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.ONE));
        cards.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.TWO));
        cards.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.THREE));
        cards.add(new Card(Color.PURPLE, Shape.DIAMOND, Quantity.THREE));
        cards.add(new Card(Color.PURPLE, Shape.WAVY, Quantity.THREE));
        cards.add(new Card(Color.PURPLE, Shape.WAVY, Quantity.ONE));
        cards.add(new Card(Color.GREEN, Shape.WAVY, Quantity.ONE));
        cards.add(new Card(Color.RED, Shape.WAVY, Quantity.ONE));
        cards.add(new Card(Color.RED, Shape.OVAL, Quantity.ONE));

        for(int i=0; 9>i;i++){
            ImageView img = new ImageView(c);
            img.setVisibility(View.VISIBLE);
            img.setContentDescription(cards.get(i).toString());
            board.add(img);
        }
    }

    @Test
    public void hasSetShouldReturnTrue() {
        assertThat(game.hasSet(cards, board)).isTrue();
    }

    @Test
    public void hasSetShouldReturnFalse() {
        ArrayList<ImageView> boardWithoutSet = new ArrayList<>();
        ArrayList<Card> cardsWithoutSet = new ArrayList<>();

        cardsWithoutSet.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.ONE));
        cardsWithoutSet.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.TWO));
        cardsWithoutSet.add(new Card(Color.GREEN, Shape.OVAL, Quantity.ONE));
        cardsWithoutSet.add(new Card(Color.GREEN, Shape.OVAL, Quantity.TWO));
        cardsWithoutSet.add(new Card(Color.RED, Shape.DIAMOND, Quantity.ONE));
        cardsWithoutSet.add(new Card(Color.RED, Shape.DIAMOND, Quantity.TWO));
        cardsWithoutSet.add(new Card(Color.RED, Shape.OVAL, Quantity.ONE));
        cardsWithoutSet.add(new Card(Color.RED, Shape.WAVY, Quantity.TWO));
        cardsWithoutSet.add(new Card(Color.PURPLE, Shape.OVAL, Quantity.ONE));

        for(int i=0; 9>i;i++){
            ImageView img = new ImageView(c);
            img.setVisibility(View.VISIBLE);
            img.setContentDescription(cards.get(i).toString());
            boardWithoutSet.add(img);
        }

        assertThat(game.hasSet(cardsWithoutSet, boardWithoutSet)).isFalse();
    }

    @Test
    public void hasSetWithAllInvisibleShouldReturnFalse() {
        for(int i = 0; i<board.size(); i++){
            board.get(i).setVisibility(View.INVISIBLE);
        }
        assertThat(game.hasSet(cards, board)).isFalse();
    }

    @Test
    public void hasSetWithThreeVisibleShouldReturnTrue() {
        for(int i = 3; i<board.size();i++){
            board.get(i).setVisibility(View.INVISIBLE);
        }
        assertThat(game.hasSet(cards, board)).isTrue();
    }

    @Test
    public void hasSetWithThreeVisibleShouldReturnFalse() {
        for(int i = 0; i<board.size()-3;i++){
            board.get(i).setVisibility(View.INVISIBLE);
        }
        assertThat(game.hasSet(cards, board)).isFalse();
    }

    @Test
    public void hasSetWithThreeCardsShouldReturnTrue() {
        ArrayList<Card> smallDeckWithSet = new ArrayList<>();

        smallDeckWithSet.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.ONE));
        smallDeckWithSet.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.TWO));
        smallDeckWithSet.add(new Card(Color.GREEN, Shape.DIAMOND, Quantity.THREE));

        assertThat(game.hasSet(smallDeckWithSet, board)).isTrue();
    }

    @Test
    public void hasSetWithThreeCardsShouldReturnFalse() {
        ArrayList<Card> smallDeckWithoutSet = new ArrayList<>();

        smallDeckWithoutSet.add(new Card(Color.GREEN, Shape.WAVY, Quantity.ONE));
        smallDeckWithoutSet.add(new Card(Color.RED, Shape.WAVY, Quantity.ONE));
        smallDeckWithoutSet.add(new Card(Color.RED, Shape.OVAL, Quantity.ONE));

        assertThat(game.hasSet(smallDeckWithoutSet, board)).isFalse();
    }

    @Test
    public void isGameOverWithAllVisibleShouldReturnFalse() {
        assertThat(game.isGameOver(board)).isFalse();
    }

    @Test
    public void isGameOverWithThreeVisibleShouldReturnFalse() {
        for(int i = 3; i<board.size();i++){
            board.get(i).setVisibility(View.INVISIBLE);
        }
        assertThat(game.isGameOver(board)).isFalse();
    }

    @Test
    public void isGameOverShouldReturnTrue() {
        for(int i = 0; i<board.size();i++){
            board.get(i).setVisibility(View.INVISIBLE);
        }
        assertThat(game.isGameOver(board)).isTrue();
    }
}