abstract class Command {
    abstract String text();

    void execute() {
        System.out.println(text());
    }
}
