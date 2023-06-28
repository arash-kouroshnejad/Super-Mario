package game.animations.items;

import core.objects.DynamicElement;
import core.objects.ElementManager;

public class CoinThread extends ElementManager {
    public CoinThread(DynamicElement coin) {
        super(coin, () -> {
            try {
                coin.swapImage(1);
                Thread.sleep(100);
                coin.swapImage(0);
                Thread.sleep(100);
            } catch (Exception ignored) {}
        });
    }
}
