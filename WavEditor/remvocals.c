#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    
    FILE *source_file;
    FILE *dest_file;

    /* Check that two user arguments are given, and that they are valid files */

    if(argc != 3) {
        fprintf(stderr, "Usage: %s <file>\n", argv[0]);
        exit(1);
    }

    if((source_file = fopen(argv[1], "rb")) == NULL) {
        fprintf(stderr,"Usage: addecho [-d delay] [-v volume_scale] sourcewav destwav");
        perror(argv[1]);
        exit(1);
    }

    if((dest_file = fopen(argv[2], "wb")) == NULL) {
        fprintf(stderr,"Usage: addecho [-d delay] [-v volume_scale] sourcewav destwav");
        perror(argv[2]);
        exit(1);
    }
    
    /* Read and write the header directly from source to dest files with no changes */

    unsigned char head[44];

    fread(head, sizeof(unsigned char), 44, source_file);
    fwrite(head, sizeof(unsigned char), 44, dest_file);

    short buffer_block[2];

    /* While there are still even amount of shorts being read, combine them and write to dest twice */

    while (fread(buffer_block, sizeof(short), 2, source_file) == 2) {
        short combined;
        combined = (buffer_block[0] - buffer_block[1])/2;
        fwrite(&combined, sizeof(short), 1, dest_file);
        fwrite(&combined, sizeof(short), 1, dest_file);
    }

    /* Close files */

    fclose(source_file);
    fclose(dest_file);

    return 0;
}