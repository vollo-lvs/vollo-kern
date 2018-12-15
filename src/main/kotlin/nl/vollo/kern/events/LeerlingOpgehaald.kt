package nl.vollo.kern.events

import nl.vollo.kern.model.Leerling

class LeerlingOpgehaald(override val body: Leerling) : Event<Leerling> {
    override val name = "LeerlingOpgehaald";
}