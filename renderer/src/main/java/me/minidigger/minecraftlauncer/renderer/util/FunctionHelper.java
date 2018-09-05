/*
 * MIT License
 *
 * Copyright (c) 2018 Ammar Ahmad
 * Copyright (c) 2018 Martin Benndorf
 * Copyright (c) 2018 Mark Vainomaa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.minidigger.minecraftlauncer.renderer.util;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.event.Event;
import javafx.event.EventHandler;

public interface FunctionHelper {

    static <T> void always(Consumer<T> consumer, T... ts) {
        Arrays.asList(ts).forEach(consumer);
    }

    static <A, B> void alwaysA(BiConsumer<A, B> consumer, A a, B... bs) {
        Arrays.asList(bs).forEach(b -> consumer.accept(a, b));
    }

    static <A, B> void alwaysB(BiConsumer<A, B> consumer, B b, A... as) {
        Arrays.asList(as).forEach(a -> consumer.accept(a, b));
    }

    static <A, B> BiConsumer<B, A> exchange(BiConsumer<A, B> consumer) {
        return (b, a) -> consumer.accept(a, b);
    }

    static <T> Consumer<T> link(Consumer<T>... consumers) {
        return t -> {
            for (Consumer<T> consumer : consumers) {
                consumer.accept(t);
            }
        };
    }

    static <T extends Event> EventHandler<T> link(EventHandler<T>... handlers) {
        return t -> {
            for (EventHandler<T> handler : handlers) {
                handler.handle(t);
            }
        };
    }

    static <A, B> Consumer<A> link1(Function<A, B> function, Consumer<B> consumer) {
        return a -> consumer.accept(function.apply(a));
    }

    static <A, B, C> BiConsumer<A, C> link2(Function<A, B> function, BiConsumer<B, C> consumer) {
        return (a, c) -> consumer.accept(function.apply(a), c);
    }

    static <A, B> Consumer<B> link2(Supplier<A> supplier, BiConsumer<A, B> consumer) {
        return b -> consumer.accept(supplier.get(), b);
    }

    static <A, B> Supplier<B> link1(Supplier<A> supplier, Function<A, B> function) {
        return () -> function.apply(supplier.get());
    }

}
