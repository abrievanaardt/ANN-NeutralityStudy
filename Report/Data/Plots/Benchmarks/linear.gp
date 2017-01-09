set xrange [-3:9]
set key bottom
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'linear.eps'

plot 0.4 * 2.7182818284590452353602874713527**(-((x-3)**2)/5) + 0.6 * 2.7182818284590452353602874713527**(-x**2) title '' lt rgb 'black' dt '-' lw '2pt', 0.4 * 2.7182818284590452353602874713527**(-((x-3)**2)/5) title '' lw '0.4pt', 0.6 * 2.7182818284590452353602874713527**(-x**2) title '' lw '0.4pt'
