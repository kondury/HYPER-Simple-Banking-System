package menu;
public class MenuItem {

    String option;
    String name;
    Actionable action;

    public MenuItem(String option, String name, Actionable action) {
        this.option = option;
        this.name = name;
        this.action = action;
    }

    public String getOption() {
        return option;
    }

    public String getName() {
        return name;
    }

    void execute() {
        action.act();
    }
}