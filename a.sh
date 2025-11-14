#!/usr/bin/env bash
set -euo pipefail

# Usage: ./find-non-ascii-with-file.sh [directory]
DIR="${1:-src/main/java/myp/proyecto2}"

if [[ ! -d "$DIR" ]]; then
  echo "Directory not found: $DIR" >&2
  exit 2
fi

# Walk files safely and use `file` output.
# Print only files where file(1) reports a text type but not "ASCII".
while IFS= read -r -d '' f; do
  # skip non-regular files just in case
  [[ -f "$f" ]] || continue

  desc=$(file -b "$f") || continue

  # If it's some kind of text but not ASCII text -> it's likely Unicode/other encoding.
  # We deliberately only use the `file` output here (no heuristics/perl).
  if [[ "$desc" == *text* && "$desc" != *ASCII* ]]; then
    printf '%s\n' "$f"
  fi
done < <(find "$DIR" -type f -print0)
