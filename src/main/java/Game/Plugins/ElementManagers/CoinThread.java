package Game.Plugins.ElementManagers;

import Core.Objects.DynamicElement;
import Core.Objects.ElementManager;

public class CoinThread extends ElementManager {
    public CoinThread(DynamicElement coin) {
        super(coin, new Runnable() {
            @Override
            public void run() {
                try {
                    coin.swapImage(1);
                    Thread.sleep(100);
                    coin.swapImage(0);
                    Thread.sleep(100);
                } catch (Exception ignored) {}
            }
        });
    }
}
