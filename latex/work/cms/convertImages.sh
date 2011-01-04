# creates the eps image
for f in *.png;
do
    echo "Processing $f"
    sam2p $f EPS: ${f/.png}.eps
done
