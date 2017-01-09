#set zrange [0: 1.2]
set format x "%.1f"
set format y "%.1f"
set xtic -1, 0.5, 1 offset -0.8
set ytic -1, 0.5, 1 offset 0.8
set xrange [-1:1]
set yrange [-1:1]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)'
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'cube.eps'
set lmargin 5
unset colorbox
set palette grey
set isosamples 200,200

splot 100 * (y - x**3)**2 + (1 - x)**2 title '' with pm3d
