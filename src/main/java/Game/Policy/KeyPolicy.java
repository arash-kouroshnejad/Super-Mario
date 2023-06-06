package Game.Policy;

public abstract class KeyPolicy extends AbstractPolicy {
    public abstract boolean enforce(int keyCode);
    public abstract boolean isEnforceable(int keyCode);
}
