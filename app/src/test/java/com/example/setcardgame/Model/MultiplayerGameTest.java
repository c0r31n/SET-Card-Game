package com.example.setcardgame.Model;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import java.util.UUID;

public class MultiplayerGameTest {
    MultiplayerGame game = new MultiplayerGame();

    @Test
    public void setNullCardIndexesStringWithEmptyArrayStringReturnsEmpty() {
        game.setNullCardIndexesString("[]");
        assertThat(game.getNullCardIndexes()).isEmpty();
    }

    @Test
    public void setNullCardIndexesStringWithArrayStringReturnsNotEmpty() {
        game.setNullCardIndexesString("[2,5]");
        assertThat(game.getNullCardIndexes()).isNotEmpty();
    }

    @Test
    public void setBoardStringWithArrayStringReturnsNotEmpty() {
        game.setBoardString("[{\"color\":\"PURPLE\",\"shape\":\"WAVY\",\"quantity\":\"THREE\"},{\"color\":\"PURPLE\",\"shape\":\"WAVY\",\"quantity\":\"TWO\"},{\"color\":\"PURPLE\",\"shape\":\"DIAMOND\",\"quantity\":\"ONE\"},{\"color\":\"PURPLE\",\"shape\":\"OVAL\",\"quantity\":\"ONE\"},{\"color\":\"PURPLE\",\"shape\":\"DIAMOND\",\"quantity\":\"THREE\"},{\"color\":\"RED\",\"shape\":\"DIAMOND\",\"quantity\":\"THREE\"},{\"color\":\"RED\",\"shape\":\"DIAMOND\",\"quantity\":\"TWO\"},{\"color\":\"PURPLE\",\"shape\":\"OVAL\",\"quantity\":\"THREE\"},{\"color\":\"GREEN\",\"shape\":\"DIAMOND\",\"quantity\":\"TWO\"}]");
        assertThat(game.getBoard()).isNotEmpty();
    }

    @Test
    public void setSelectedCardIndexesStringWithEmptyArrayStringReturnsEmpty() {
        game.setSelectedCardIndexesString("[]");
        assertThat(game.getSelectedCardIndexes()).isEmpty();
    }

    @Test
    public void setSelectedCardIndexesStringWithArrayStringReturnsNotEmpty() {
        game.setSelectedCardIndexesString("[1]");
        assertThat(game.getSelectedCardIndexes()).isNotEmpty();
    }

    @Test
    public void setPointsStringWithNullPlayerReturnsEmpty() {
        game.setPointsString("{\"88f19a5a-134b-450b-a894-21abbb2ad664\":0,\"7d6e1f60-9a15-4228-bd07-ca3b5c272045\":0}");
        assertThat(game.getPoints()).isEmpty();
    }

    @Test
    public void setPointsStringWithArrayStringReturnsNotEmpty() {
        game.setPlayer1(UUID.randomUUID());
        game.setPlayer2(UUID.randomUUID());
        game.setPointsString("{\"88f19a5a-134b-450b-a894-21abbb2ad664\":0,\"7d6e1f60-9a15-4228-bd07-ca3b5c272045\":0}");
        assertThat(game.getPoints()).isNotEmpty();
    }
}