package org.example.api.Parser;

import org.example.api.Command.ICommand;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuildParser<T extends ICommand> extends AbstractParser {

    private Supplier<T> commandFactory;
    private int minArgs = 0;
    private String minArgsMessage = "";

    private Set<String> allowedFlags = new HashSet<>();
    private Set<String> valuedFlags  = new HashSet<>();

    private record FlagValidator(String flag, Predicate<String> predicate, String message) {}
    private List<FlagValidator> flagValidators = new ArrayList<>();

    private record ArgValidator(int index, Predicate<String> predicate, String message) {}
    private List<ArgValidator> validators = new ArrayList<>();

    public static <T extends ICommand> BuildParser<T> of(Supplier<T> commandFactory) {
        BuildParser<T> parser = new BuildParser<>();
        parser.commandFactory = commandFactory;
        return parser;
    }

    public BuildParser<T> allowed(String ...allowed)
    {
        this.allowedFlags.addAll(List.of(allowed));
        return this;
    }

    public BuildParser<T> valued(String ...valued)
    {
        this.allowedFlags.addAll(List.of(valued));
        this.valuedFlags.addAll(List.of(valued));
        return this;
    }

    public BuildParser<T> minArgs(int min, String message)
    {
        this.minArgs = min;
        this.minArgsMessage = message;
        return this;
    }

    public BuildParser<T> validateArg(
            int index,
            Predicate<String> predicate,
            String message)
    {
        validators.add(new ArgValidator(index, predicate, message));
        return this;
    }

    public BuildParser<T> validateFlag(
            String flag,
            Predicate<String> predicate,
            String message)
    {
        this.allowedFlags.add(flag);
        flagValidators.add(new FlagValidator(flag, predicate, message));
        return this;
    }

    public IParser build() {
        return this;
    }

    protected T parse() throws Exception {
        T command = commandFactory.get();
        setAllowed(allowedFlags.toArray(new String[0]));
        setValued(valuedFlags.toArray(new String[0]));

        var parsed = consumeArgs(command);
        command.setArgs(parsed.args());

        //minimum arguments check
        if (command.getArgs().size() < this.minArgs)
            throw new Exception("ERROR: Commands requires at lest["
                    + this.minArgs
                    + "] arguments");

        for (ArgValidator v : validators)
        {
            if (v.index() < parsed.args().size())
            {
                String arg = parsed.args().get(v.index());
                if (!v.predicate().test(arg))
                    throw new Exception(v.message());
            }
        }

        for (FlagValidator v : flagValidators) {
            if (command.has(v.flag())) {
                String value = command.getValue(v.flag());
                if (!v.predicate().test(value))
                    throw new Exception(v.message());
            }
        }

        return command;
    }
}
