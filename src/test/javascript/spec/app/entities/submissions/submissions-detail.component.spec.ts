/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { SubmissionsDetailComponent } from 'app/entities/submissions/submissions-detail.component';
import { Submissions } from 'app/shared/model/submissions.model';

describe('Component Tests', () => {
    describe('Submissions Management Detail Component', () => {
        let comp: SubmissionsDetailComponent;
        let fixture: ComponentFixture<SubmissionsDetailComponent>;
        const route = ({ data: of({ submissions: new Submissions(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [SubmissionsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SubmissionsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubmissionsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.submissions).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
