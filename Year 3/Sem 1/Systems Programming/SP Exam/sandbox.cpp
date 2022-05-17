struct file_handle {
	FILE * file;
	file_handle(const char * filename) {
		file = fopen(filename, "r");
		if (!file) { exit(EXIT_FAILURE); }
	}
	~file_handle() { fclose(file); }
};
typedef struct file_handle file_handle;

int main() {
	file_handle f("./file.txt");
	...
}