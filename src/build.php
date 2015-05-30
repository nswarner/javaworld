#! /usr/bin/php
<?php
/*

	After spending many hours compiling this project repeatedly, I've found
	that it's rather annoying to wait ~10 seconds each compile when I've only
	changed a single character in a file. Therefore, this build script attempts
	to only compile files which have a more recent timestamp than the previous
	build's timestamp file.

	While this looks like a good idea, Java will still compile any secondary
	files which are referenced by the files which have changed. Therefore, this
	script will still make the compiles go faster, but the java compiler may
	still compile 3/4 of the files when only a single file has changed.

	The only other issue relates to the accuracy of the timestamp as listed by
	the `ls -lst` command. Since this is only precise to the minute, attempting
	to build several times in a single minute will not have the desired effect.

*/

// Find the most recent timestamp file
$timestamp = `ls -lst ~/dev/muds/javaworld/src/ | grep timestamp`;

// Find the actual timestamp for the file
preg_match('/[a-zA-Z]+[ ]+[0-9]+ [0-9]{2}:[0-9]{2}/', $timestamp, $matches);

$matches[0] = strtotime($matches[0]);

// Let's find the filenames
$pattern = '/([a-zA-Z]+[ ]+[0-9]+ [0-9]{2}:[0-9]{2}) [^ ]*.java/';

preg_match_all($pattern, `ls -lst`, $timestamps);

$pattern = '/[0-9]{2}:[0-9]{2} ([^ ]*.java)/';

preg_match_all($pattern, `ls -lst`, $filematches);

$file_list = "";

foreach($timestamps[1] as $key => $value) {

	$value = strtotime($value);

	if ($value >= $matches[0]) {

		$file_list .= $filematches[1][$key] . " ";
	}
}

`touch timestamp.perm`;

echo("Compiling file list: " . $file_list . "\n\r");
`javac $file_list -d ../classes/ 2> error.log`;

echo("Build complete.\n\n");

?>
