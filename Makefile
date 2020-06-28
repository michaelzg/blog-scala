
idea:
	mill mill.scalalib.GenIdea/idea

clean:
	mill clean

format:
	mill mill.scalalib.scalafmt.ScalafmtModule/reformatAll __.sources

compile:
	mill __.compile

test:
	mill __.test

updates:
	# "Doesn't apply to $ivy dependencies used in the build definition itself."
	mill mill.scalalib.Dependency/updates
