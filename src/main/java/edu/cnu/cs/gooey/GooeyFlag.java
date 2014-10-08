package edu.cnu.cs.gooey;

public enum GooeyFlag {
	MATCH_BY_NAME, // name- or text-based search. text takes precedence when both or none are present.
	SEARCH_FLAT    // nested- or flat-based search. nested takes precedence when both or none are present.
}
