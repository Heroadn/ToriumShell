package org.example.api.Parser;

import org.example.api.Command.ICommand;

import java.util.List;

public interface IParser
{
    ICommand parse(List<Token> tokens) throws Exception;
}
