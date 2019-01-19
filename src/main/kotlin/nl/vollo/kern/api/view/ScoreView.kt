package nl.vollo.kern.api.view

import com.fasterxml.jackson.annotation.JsonIgnore
import nl.vollo.kern.model.Score

class ScoreView(@JsonIgnore val score: Score) {
    val cijferScore get() = score.cijferScore
    val letterScore get() = score.letterScore
    val datum get() = score.toetsafname.datum
    val omschrijving get() = score.toetsafname.omschrijving
    val toetsOmschrijving get() = score.toetsafname.toets.omschrijving
    val toetsSoort get() = score.toetsafname.toets.soort
    val toetsSoortScore get() = score.toetsafname.toets.soortScore
    val roepnaam get() = score.leerling.roepnaam
    val tussenvoegsel get() = score.leerling.tussenvoegsel
    val achternaam get() = score.leerling.achternaam
    val leerlingId get() = score.leerling.id
    val geslacht get() = score.leerling.geslacht
    val geboortedatum get() = score.leerling.geboortedatum
}