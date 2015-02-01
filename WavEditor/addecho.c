#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char *argv[]) {

	int delay;
	int volume_scale;
	int opt;
	int counter;

    /* Set default values for delay and volume_scale */

	delay = 8000;
	volume_scale = 4;

	FILE *source_file;
	FILE *dest_file;

    /* While optind is less than total amount of arguments, evaluate optional arguments if they exist,
       and  check that files are valid */

	while (optind < argc) {
		if ((opt = getopt(argc, argv, "d:v:")) != -1) {
			switch (opt) {
			case 'd':
				delay = atoi(optarg);
				break;
			case 'v':
				volume_scale = atoi(optarg);
				break;
			default:
                fprintf(stderr,"Usage: addecho [-d delay] [-v volume_scale] sourcewav destwav");
                exit(0);
				break;
			}
		}
		else {
			if((source_file = fopen(argv[optind], "rb")) == NULL) {
                fprintf(stderr,"Usage: addecho [-d delay] [-v volume_scale] sourcewav destwav");
        		perror(argv[optind]);
        		exit(1);
    		}

    		optind++;

    		if((dest_file = fopen(argv[optind], "wb")) == NULL) {
                fprintf(stderr,"Usage: addecho [-d delay] [-v volume_scale] sourcewav destwav");
        		perror(argv[optind]);
        		exit(1);
    		}

            optind++;
		}
	}

    /* Read header from source file, edit only at positions 2 and 20, and then write to dest file */

    #define HEADER_SIZE 22
    short header[HEADER_SIZE];
    unsigned int *sizeptr;

    fread(header, sizeof(short), HEADER_SIZE, source_file);

    sizeptr = (unsigned int *)(header + 2);
    *sizeptr = *sizeptr + delay * 2;

    sizeptr = (unsigned int *)(header + 20);
    *sizeptr = *sizeptr + delay * 2;
    
    fwrite(header, sizeof(short), HEADER_SIZE, dest_file);

    short echo_buffer[delay];
    short orig_buffer[delay];
    short mixed_buffer[delay];
    int orig_num_samples;
    int echo_num_samples;

    /* Implementation of algorithm to layer an echo over an original wav file
       First check for case of when the delay size is greater than size of orig,
       then write first orig block to dest file, and set echo block to equal orig */

    if ((orig_num_samples = (fread(orig_buffer, sizeof(short), delay, source_file))) == delay) {
        fwrite(orig_buffer, sizeof(short), delay, dest_file);

        for (counter = 0; counter < delay; counter++) {
            echo_buffer[counter] = orig_buffer[counter]/volume_scale;
        }
    }
    else {
        for (counter = 0; counter < orig_num_samples; counter++) {
            echo_buffer[counter] = orig_buffer[counter]/volume_scale;
        }

        for (counter = orig_num_samples; counter < delay; counter++) {
            orig_buffer[counter] = 0;
        }

        fwrite(orig_buffer, sizeof(short), delay, dest_file);
        fwrite(echo_buffer, sizeof(short), orig_num_samples, dest_file);
    }

    /* Then, while orig is not finished being read,  if each sample being read from orig matches
       length of delay, then simply mix it with echo block, and then set echo block to equal the
       new orig. If orig is smaller than length of delay, then fill up orig with 0s until it
       matches length of delay, mix it with echo, and then set echo to orig again (this time 
       echo will also have a smaller size). Write the mixed block to dest file */

    while ((orig_num_samples = (fread(orig_buffer, sizeof(short), delay, source_file))) != 0) {
    	if (orig_num_samples == delay) {
            for (counter = 0; counter < delay; counter++) {
                mixed_buffer[counter] = orig_buffer[counter] + echo_buffer[counter];
            }

            for (counter = 0; counter < delay; counter++) {
                echo_buffer[counter] = orig_buffer[counter]/volume_scale;
                echo_num_samples = delay;
            }
        }
        else {
            for (counter = orig_num_samples; counter < delay; counter++) {
                orig_buffer[counter] = 0;
            }

            for (counter = 0; counter < delay; counter++) {
                mixed_buffer[counter] = orig_buffer[counter] + echo_buffer[counter];
            }
            
            for (counter = 0; counter < orig_num_samples; counter++) {
                echo_buffer[counter] = orig_buffer[counter]/volume_scale;
                echo_num_samples = orig_num_samples;
            }
        }

        fwrite(mixed_buffer, sizeof(short), delay, dest_file);   
    }

    /* Finally, if the original file happened to be divisible perfectly by length of delay, 
       simply write delay amount of echo to dest file. Otherwise, write the amount of samples
       in echo to dest file */

    if (echo_num_samples == delay) {
        fwrite(echo_buffer, sizeof(short), delay, dest_file);
    }
    else {
        fwrite(echo_buffer, sizeof(short), echo_num_samples, dest_file);
    }

    /* Close files */

	fclose(source_file);
    fclose(dest_file);

	return 0;
}