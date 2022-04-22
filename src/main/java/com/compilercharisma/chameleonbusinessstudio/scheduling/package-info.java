/**
 * well, this package is in a bit of a pickle.
 * 
 * appointments can have many tags, each of which can have many values
 * this is best represented as Map<String, Set<String>>
 * but, JPA cannot translate this to a database entry, so we must represent
 * entity tags as Set<{String, String}>
 * JSON can handle this perfectly well as an object with {String: String[]},
 * which is what we will likely use upon porting to Vendia's data layer
 * ergo, I have implemented a translator to convert back-and-forth between data
 * formats.
 * 
 * note that this package is in a state of flux, but the Controller & Service
 * should remain relatively stable
 */
package com.compilercharisma.chameleonbusinessstudio.scheduling;
