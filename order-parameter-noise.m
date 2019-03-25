#! /bin/octave -qf

defaultVelocity = 0.03;
radius = 0;
periodic = true;
duration=1000;

simOutputFile = sprintf("./output.txt");
delta_t=1;
rc=0.5;

# data from Paper
# N, L, grap symbol, M
#simulation_data_values={40, 3.1, 's',3; 100, 5, '+',4; 400, 10, 'x',8; 4000, 31.6, '^',30; 10000, 50, 'd',40}
simulation_data_values={40, 3.1, 's',6; 100, 5, '+',8; 400, 10, 'x',18}

etha_values=0:0.25:5;

times=20;
hold on;

for i=1:rows(simulation_data_values)
       N=simulation_data_values{i,1};
       L=simulation_data_values{i,2};
       M=simulation_data_values{i,4};

       marker=simulation_data_values{i,3};

       outputFileName= sprintf("./graphics/va_N=%d.txt",N);
       outputFile = fopen(outputFileName, 'w');

       for etha=etha_values
              etha
              va_values=zeros(1,times);

              for k=1:times
                fid = fopen(simOutputFile,'r');
                fgetl(fid); % Omit execution time line
                va_values(k)=strread(fgetl(fid), '%n');
                va_values(k)
              endfor

              va_mean=mean(va_values); 
              dlmwrite(outputFile, [etha,va_mean], "  "); 
              plot(etha,va_mean,"marker",marker);
       endfor
       fclose(outputFile);
endfor;

xlabel('etha');
ylabel('Va');
legend('N=40','N=100','N=400');
hold off;

print -djpg ./graphics/va_curves.jpg
