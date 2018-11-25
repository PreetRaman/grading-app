/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { SubmissionsService } from 'app/entities/submissions/submissions.service';
import { ISubmissions, Submissions, Course, Subject, Exercise } from 'app/shared/model/submissions.model';

describe('Service Tests', () => {
    describe('Submissions Service', () => {
        let injector: TestBed;
        let service: SubmissionsService;
        let httpMock: HttpTestingController;
        let elemDefault: ISubmissions;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SubmissionsService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Submissions(0, 'AAAAAAA', 'AAAAAAA', Course.GSD, Subject.JAVA, Exercise.E1, 'image/png', 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Submissions', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Submissions(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Submissions', async () => {
                const returnedFromService = Object.assign(
                    {
                        fdaiNumber: 'BBBBBB',
                        name: 'BBBBBB',
                        course: 'BBBBBB',
                        subject: 'BBBBBB',
                        exercises: 'BBBBBB',
                        files: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Submissions', async () => {
                const returnedFromService = Object.assign(
                    {
                        fdaiNumber: 'BBBBBB',
                        name: 'BBBBBB',
                        course: 'BBBBBB',
                        subject: 'BBBBBB',
                        exercises: 'BBBBBB',
                        files: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Submissions', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
