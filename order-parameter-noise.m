#! /bin/octave -qf

defaultVelocity = 0.03;
radius = 0;
periodic = true;
duration=1000;

delta_t=1;
rc=0.5;

simulation_data_values={40, 3.1, 's', 6, 'r'; 100, 5, '+', 8, 'b'; 400, 10, 'x', 18, 'g'};

etha_values=0:0.25:5;

hold on;

for i=1:rows(simulation_data_values)
       N=simulation_data_values{i,1};
       L=simulation_data_values{i,2};
       M=simulation_data_values{i,4};
       marker=simulation_data_values{i,3};

       outputFileName = sprintf("./output/N=%d-L=%d-M=%d.txt", N, L, M);
       outputFile = fopen(outputFileName, 'r');

       va_plot_values = zeros(size(etha_values));
       plot_index = 1;

       while ~feof(outputFile)
        % Read etha and va
        tline = fgetl(outputFile);
        data = strsplit(tline);
        va_mean = str2num(data{1,2});
        va_plot_values(plot_index) = va_mean;

        % Read std
        tline = fgetl(outputFile);
        data = strsplit(tline);
        stderr = data{1,2};

        plot_index += 1;
      end

      plot(etha_values, va_plot_values, "marker", marker, "linestyle", "none", "color", simulation_data_values{i,5});
      fclose(outputFile);
endfor;

xlabel('etha');
ylabel('Va');
axis([0 5.0 0 1.0])
title("El valor absoluto de la velocidad media frente al ruido para una densidad fija")
grid on
legend('N=40','N=100','N=400');
hold off;

% print -djpg sprintf("./graphics/va_curves_duration=%d.jpg", duration)
print(sprintf("./graphics/va_curves_duration=%d.jpg", duration), "-djpg")
