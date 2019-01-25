/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ActiveUsersService } from 'app/entities/active-users/active-users.service';
import { IActiveUsers, ActiveUsers } from 'app/shared/model/active-users.model';

describe('Service Tests', () => {
    describe('ActiveUsers Service', () => {
        let injector: TestBed;
        let service: ActiveUsersService;
        let httpMock: HttpTestingController;
        let elemDefault: IActiveUsers;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ActiveUsersService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new ActiveUsers(0, 'AAAAAAA', currentDate, currentDate, false);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        login_time: currentDate.format(DATE_TIME_FORMAT),
                        logout_time: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a ActiveUsers', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        login_time: currentDate.format(DATE_TIME_FORMAT),
                        logout_time: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        login_time: currentDate,
                        logout_time: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new ActiveUsers(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a ActiveUsers', async () => {
                const returnedFromService = Object.assign(
                    {
                        username: 'BBBBBB',
                        login_time: currentDate.format(DATE_TIME_FORMAT),
                        logout_time: currentDate.format(DATE_TIME_FORMAT),
                        active: true
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        login_time: currentDate,
                        logout_time: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of ActiveUsers', async () => {
                const returnedFromService = Object.assign(
                    {
                        username: 'BBBBBB',
                        login_time: currentDate.format(DATE_TIME_FORMAT),
                        logout_time: currentDate.format(DATE_TIME_FORMAT),
                        active: true
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        login_time: currentDate,
                        logout_time: currentDate
                    },
                    returnedFromService
                );
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

            it('should delete a ActiveUsers', async () => {
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
