/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradingAppTestModule } from '../../../test.module';
import { SubmissionsComponent } from 'app/entities/submissions/submissions.component';
import { SubmissionsService } from 'app/entities/submissions/submissions.service';
import { Submissions } from 'app/shared/model/submissions.model';

describe('Component Tests', () => {
    describe('Submissions Management Component', () => {
        let comp: SubmissionsComponent;
        let fixture: ComponentFixture<SubmissionsComponent>;
        let service: SubmissionsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [SubmissionsComponent],
                providers: []
            })
                .overrideTemplate(SubmissionsComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SubmissionsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubmissionsService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Submissions(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.submissions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
