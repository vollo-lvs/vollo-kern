package nl.vollo.kern.events

interface Event<T> {
    val name: String;
    val body: T;
}
