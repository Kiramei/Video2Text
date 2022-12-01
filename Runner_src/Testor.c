#pragma comment(lib, "WINMM.LIB")
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <vfw.h>
#include <mmsystem.h>
#include <string.h>
#include <time.h>
int main()
{
	void recursur();
	void progress();
	progress();
	return 0;
}
void recursur()
{
	HANDLE hout;
	COORD coord;
	coord.X = 0;
	coord.Y = 0;
	hout = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleCursorPosition(hout, coord);
}
void progress()
{
	int i = 0, caf = 33;
	FILE *fp;
	clock_t stime = 0, ftime = 0;
	char seat[] = "output\\0.txt", p[50];
	char string[25], fws[5], fhs[5];
	char *ptr, *retptr;
	char cm[30];
	int is = 0;
	int ftp, fw, fh;
	float fps;
	FILE *fpt;
	fpt = fopen("output\\config.log", "r");
	fgets(cm, 30, fpt);
	ptr = cm;
	float fl[4];
	while ((retptr = strtok(ptr, ",")) != NULL)
	{
		fl[is] = atof(retptr);
		ptr = NULL;
		is++;
	}
	ftp = (int)fl[0];
	fps = fl[1];
	fw = (int)fl[2];
	fh = (int)fl[3];
	int reso = fw * fh;
	char buf[reso];
	itoa(fw, fws, 10);
	itoa(fh + 1, fhs, 10);
	strcpy(p, "mode con cols=");
	strcat(p, fws);
	strcat(p, " lines=");
	strcat(p, fhs);
	system(p);
	printf("-----CVP ASCII art player-----\nPress Enter to play.\n");
	getchar();
	system("cls");
	PlaySound("output\\Audinfo.wav", NULL, SND_FILENAME | SND_ASYNC);
	stime = clock();
	int s = 0;
	while (i < ftp)
	{
		if (s % (int)fps == 0 && i != 0)
		{
			caf += 1000 - (int)(caf * fps);
			s++;
		}
		else
		{
			caf = (int)(1000.0 / fps);
		}
		if (s % 10 == 0)
			caf -= (int)(10 * fps * caf) % 10;
		if (s % 100 == 0)
			caf -= (int)(100 * fps * caf) % 10;
		strcpy(seat, "output\\");
		itoa(i, string, 10);
		strcat(seat, string);
		strcat(seat, ".txt");
		ftime = clock();
		if ((ftime - stime) >= caf)
		{
			i++;
			fp = fopen(seat, "r");
			fread(buf, sizeof(buf), 1, fp);
			buf[reso + 1] = '\0';
			fclose(fp);
			fprintf(stdout, "%s\n\n", buf);
			fprintf(stdout, "Frame:%d", i);
			stime += caf;
			recursur();
		}
	}
	system("cls");
	printf(
		"-----CVP ASCII art player-----\nBased on chuan.\nMade by Kiramei\n\n");
	printf("Press Q to Exit.\nPress R to Review.\n");
	char c;
	c = getchar();
	if (c == 'Q')
		return;
	else if (c == 'R')
		progress();
	else
		return;
}
