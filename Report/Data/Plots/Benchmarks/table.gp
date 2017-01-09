#set zrange [0: 1.2]
set format z "%.1f"
set xtic -5, 2, 5 offset -0.8
set ytic -5, 2, 5 offset 0.8
set xrange [-5:5]
set yrange [-5:5]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)'
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'table.eps'
set lmargin 5
unset colorbox
set palette negative grey
set isosamples 100,100

splot abs(x) > 2.5 || abs(y) > 2.5 ? 1 : abs(x) < 2.5 || abs(y) < 2.5 ? 0 : 0.5 title '' with pm3d
