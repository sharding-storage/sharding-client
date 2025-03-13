package ru.itmo.vk.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
public abstract class AbstractNode {
    private final String address;
}
