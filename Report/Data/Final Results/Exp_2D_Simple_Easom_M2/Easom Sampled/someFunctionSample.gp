#set zrange [0: 1.2]
#set format z "%.1f"
#set xtic -100, 50, 100 offset -1
#set ytic -100, 50, 100 offset 1
#set ztic 0, 50, 200
#set xrange [-100:100]
#set yrange [-100:100]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)' offset -1.2
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'someFunctionSample2.eps'
set lmargin 6
set isosamples 80,80
set pm3d interpolate 0,0
unset colorbox
set palette grey
set view map

splot 'Easom.dat' title '' with pm3d, for [i=1:20] 'Walk ' .i.'.dat' title '' with lines lc 'black' dt i
