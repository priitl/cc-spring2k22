#!/bin/bash
src_path=$1
file_type=$2
out_dir=$src_path/../out
out_file=$out_dir/Player.$file_type

# ensure out directory exists
[[ -d $out_dir ]] || mkdir "$out_dir"

# clear out_file
rm -rf "$out_file"

# copy all import statements first
find "$src_path" ! -path '*/test/*' -type f -name "*.$file_type" -exec grep -E 'import' {} \; | grep -v '^import com.priitlaht' >> "$out_file"

# copy everything else excluding package and import statements
find "$src_path" ! -path '*/test/*' -type f -name "*.$file_type" -exec grep -Ev 'package|import' {} \; | sed -e 's/^public class/class/' -e 's/^public final class/class/' -e 's/^public abstract class/abstract class/' -e 's/^public interface/interface/' >> "$out_file"
java -jar $src_path/../cc-tools/lombok.jar delombok out -d $out_dir/../merged-src