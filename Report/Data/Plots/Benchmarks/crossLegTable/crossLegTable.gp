#set zrange [-1: 0]
#set format z "%.1f"
set xtic -10, 5, 10 offset -0.8
set ytic -10, 5, 10 offset 0.8
set ztic 0,0.2, 1
set xrange [-10:10]
set yrange [-10:10]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)'
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'crossLegTable.eps'
set lmargin 5
unset colorbox
set palette negative grey

#-1/((abs(exp(abs(100 - sqrt(x**2 + y**2)/pi)) * sin(x) * sin(y)) + 1)**0.1)

splot 'crossLegTable.dat' title '' with pm3d
