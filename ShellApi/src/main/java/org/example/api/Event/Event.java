package org.example.api.Event;

public sealed interface Event
        permits CommandExecuted, CommandReceived, CommandFailed, PluginLoaded, DirectoryChanged {}
